package in.nic.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import saml.SamlAuthnRequestBuilder;
import util.SamlBase64Util;

//SAML login controller

@Path("/saml")
public class SamlController {

	private static final Logger logger = LogManager.getLogger(SamlController.class);

	private static final Properties prop = new Properties();

	static {
	    try (InputStream input = SamlController.class
	            .getClassLoader()
	            .getResourceAsStream("config.properties")) {

	        prop.load(input);

	    } catch (Exception e) {
	        throw new RuntimeException("Unable to load config.properties", e);
	    }
	}
    
	

	private static final String IDP_LOGIN_URL =
	        prop.getProperty("IDP_LOGIN_URL");

	private static final String LOGOUT_IDP_ENDPOINT =
	        prop.getProperty("LOGOUT_IDP_ENDPOINT");
	
    
    @GET
    @Path("/login")
    public Response login() {		
        logger.info("Initiating SAML SSO login process");
        try {
            // Building XML for login AuthnRequest
            String samlXml = SamlAuthnRequestBuilder.build();
            logger.debug("Constructed AuthnRequest XML: length={} characters", samlXml != null ? samlXml.length() : 0);

            // Convert XML to Base64
            String base64Request = SamlBase64Util.encodeXmlToBase64(samlXml);

            // URL encode Base64 required for HTTP-Redirect binding
            String encodedRequest = URLEncoder.encode(base64Request, StandardCharsets.UTF_8);

//            String targetIdpUrl = (IDP_LOGIN_URL != null && !IDP_LOGIN_URL.isBlank()) 
//                    ? IDP_LOGIN_URL 
//                    : "https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogin";

            
            String targetIdpUrl = (IDP_LOGIN_URL != null && !IDP_LOGIN_URL.isBlank()) 
                    ? IDP_LOGIN_URL 
                    : "https://parichay.staging.nic.in/v1/samlLogin";

            String redirectUrl = targetIdpUrl
                    + "?SAMLRequest=" + encodedRequest          
                    + "&RelayState=RelayState";

                        
            logger.info("Redirecting client to SAML IdP login endpoint: {}", targetIdpUrl);
            logger.debug("Full SAML redirect URL: {}", redirectUrl);
            
            return Response.temporaryRedirect(java.net.URI.create(redirectUrl)).build();
        } catch (Exception e) {
            logger.error("Failed to generate SAML login request", e);
            return Response.serverError().entity("Error generating SAML login request").build();
        }
    }
    
    // SAML logout controller
    @GET
    @Path("/requestlogout")
    public Response logout(@QueryParam("nameId") String nameId,
            @QueryParam("sessionIndex") String sessionIndex,
            @Context HttpServletRequest request) {
    	
        logger.info("Initiating SAML Single Logout (SLO) request: nameId={}, sessionIndex={}", nameId, sessionIndex);
        try {
            // Build LogoutRequest XML from session
            String samlXml = SamlAuthnRequestBuilder.build2(request);
            logger.debug("Constructed LogoutRequest XML: length={} characters", samlXml != null ? samlXml.length() : 0);

            // Convert XML to Base64
            String base64Request = SamlBase64Util.encodeXmlToBase64(samlXml);

            // URL encode Base64 required for redirect call
            String encodedRequest = URLEncoder.encode(base64Request, StandardCharsets.UTF_8);

//            String targetLogoutUrl = (LOGOUT_IDP_ENDPOINT != null && !LOGOUT_IDP_ENDPOINT.isBlank()) 
//                    ? LOGOUT_IDP_ENDPOINT 
//                    : "https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogout";

            String targetLogoutUrl = (LOGOUT_IDP_ENDPOINT != null && !LOGOUT_IDP_ENDPOINT.isBlank()) 
                    ? LOGOUT_IDP_ENDPOINT 
                    : "https://parichay.staging.nic.in/v1/samlLogout";

            
            String redirectUrl = targetLogoutUrl + "?SAMLRequest=" + encodedRequest;
            logger.info("Redirecting client to SAML IdP logout endpoint: {}", targetLogoutUrl);
            logger.debug("Full SAML logout redirect URL: {}", redirectUrl);

            return Response.temporaryRedirect(java.net.URI.create(redirectUrl)).build();
        } catch (Exception e) {
            logger.error("Failed to generate SAML logout request", e);
            return Response.serverError().entity("Error generating SAML logout request").build();
        }
    }
}


