package in.nic.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
	
    private static final String IDP_LOGIN_URL =
    		"https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogin";
    
    private static final String LOGOUT_IDP_ENDPOINT =
    		"https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogout";
   
    
    
    @GET
    @Path("/login")
    public Response login() {		

    	logger.debug("Inside saml/login");
        // Building XML for login AuthnRequest
        String samlXml = SamlAuthnRequestBuilder.build(); // returns XML string

        // Convert XML to Base64
        String base64Request = SamlBase64Util.encodeXmlToBase64(samlXml);//base 64 xml

        // URL encode Base64 i.e required for redirect 
        String encodedRequest = URLEncoder.encode(		//encode base64Request
                base64Request,
                StandardCharsets.UTF_8
        );
        logger.debug("encodedRequest {}",encodedRequest);

        // Build redirect URL for login 
        String redirectUrl = IDP_LOGIN_URL
                + "?SAMLRequest=" + encodedRequest          
                + "&RelayState=RelayState";

        logger.debug("redirectUrl {}",redirectUrl);
        
        // redirect call to Parichay login Page
        return  Response.temporaryRedirect(java.net.URI.create(redirectUrl)).build();
        		  
    }
    
    
  //SAML logout controller
    
    @GET
    @Path("/requestlogout")
    public Response logout(@QueryParam("nameId") String nameId,
    		@QueryParam("sessionIndex") String sessionIndex,
            @Context HttpServletRequest request) {		//inject HttpServletRequest in jersey
    	
    	logger.debug("nameId: {} | sessionIndex: {}", nameId, sessionIndex);
    	
    	 // Build AuthnRequest XML
        String samlXml = SamlAuthnRequestBuilder.build2(request); // returns logout XML as string

        // Convert XML to Base64
        String base64Request = SamlBase64Util.encodeXmlToBase64(samlXml);//base 64 xml

        // URL encode Base64 i.e required for redirect call
        String encodedRequest = URLEncoder.encode(
                base64Request,
                StandardCharsets.UTF_8
        );
        logger.debug("encodedRequest {}",encodedRequest);

        // Build redirect URL for login 
        String redirectUrl = LOGOUT_IDP_ENDPOINT
                + "?SAMLRequest=" + encodedRequest ;         
     //;

        logger.debug("redirectUrl {}",redirectUrl);
        // Send redirect
        return  Response.temporaryRedirect(java.net.URI.create(redirectUrl)).build();
        	
    		
       
    }
    
    
}


