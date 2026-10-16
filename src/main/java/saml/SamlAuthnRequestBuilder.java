package saml;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SamlAuthnRequestBuilder {

	private static final Logger logger = LogManager.getLogger(SamlAuthnRequestBuilder.class);
	
	
	
	 public static final Properties prop = new Properties();

	    static {
	        try (InputStream input = SamlAuthnRequestBuilder.class
	                .getClassLoader()
	                .getResourceAsStream("config.properties")) {

	            if (input == null) {
	                throw new RuntimeException("config.properties not found");
	            }

	            prop.load(input);

	        } catch (Exception e) {
	            throw new RuntimeException(
	                    "Failed to load config.properties", e);
	        }
	    }
	    
	    
	 // Client name
	    private static final String ISSUER =
	    		SamlAuthnRequestBuilder.prop.getProperty("ISSUER");

	    // Callback URL / Home URL
	    private static final String ACS_URL =
	    		SamlAuthnRequestBuilder.prop.getProperty("ACS_URL");

	    // Redirect URL
	    private static final String DESTINATION =
	    		SamlAuthnRequestBuilder.prop.getProperty("DESTINATION");

	    // Logout URL
	    private static final String LOGOUT_IDP_ENDPOINT =
	    		SamlAuthnRequestBuilder.prop.getProperty("LOGOUT_IDP_ENDPOINT");
	    
//	//client name
//    private static final String ISSUER = "samljpclient";
//    
//    // callback url/home url
//    private static final String ACS_URL =
//            "http://jpmeripehchaan.staging.nic.in/saml/home";
//
//    //redirect url
//    private static final String DESTINATION =
//            "https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogin";
//
//    //redirect url
//    private static final String LOGOUT_IDP_ENDPOINT = 
//    		"https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogout";
// 
	    
    // Building XML for login request
    public static String build() {
        String requestId = "_" + UUID.randomUUID();
        String issueInstant = Instant.now().toString();

        logger.info("Constructing SAML AuthnRequest: ID={}, Issuer={}, ACS_URL={}", requestId, ISSUER, ACS_URL);
        logger.debug("AuthnRequest details: Destination={}, IssueInstant={}", DESTINATION, issueInstant);

        String authnRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<saml2p:AuthnRequest "
            + "xmlns:saml2p=\"urn:oasis:names:tc:SAML:2.0:protocol\" "
            + "AssertionConsumerServiceURL=\"" + ACS_URL + "\" "
            + "Destination=\"" + DESTINATION + "\" "
            + "ID=\"" + requestId + "\" "
            + "IssueInstant=\"" + issueInstant + "\" "
            + "ProtocolBinding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\" "
            + "Version=\"2.0\">"
            + "<saml2:Issuer "
            + "xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">"
            + ISSUER
            + "</saml2:Issuer>"
            + "<saml2p:NameIDPolicy "
            + "AllowCreate=\"true\" "
            + "Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\"/>"
            + "<saml2p:RequestedAuthnContext Comparison=\"exact\">"
            + "<saml2:AuthnContextClassRef "
            + "xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">"
            + "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"
            + "</saml2:AuthnContextClassRef>"
            + "</saml2p:RequestedAuthnContext>"
            + "</saml2p:AuthnRequest>";

        logger.debug("Successfully generated AuthnRequest XML payload");
        return authnRequestXml;
    }
    
    // Building XML for logout request
    public static String build2(HttpServletRequest request) {		
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("Attempted to build SAML LogoutRequest with null HTTP session");
            throw new IllegalStateException("Active session required to build SAML LogoutRequest");
        }

        String requestId = "_" + UUID.randomUUID();
        String issueInstant = Instant.now().toString();
    
        String samlNameId = (String) session.getAttribute("SAML_NAME_ID");
        String samlSessionIndex = (String) session.getAttribute("SAML_SESSION_INDEX");
        Object notOnOrAfterObj = session.getAttribute("notOnOrAfter");
        String notOnOrAfter = notOnOrAfterObj != null ? notOnOrAfterObj.toString() : "";
      
        logger.info("Constructing SAML LogoutRequest: ID={}, NameID={}, SessionIndex={}", 
                requestId, samlNameId, samlSessionIndex);
        logger.debug("LogoutRequest details: Destination={}, IssueInstant={}, NotOnOrAfter={}", 
                LOGOUT_IDP_ENDPOINT, issueInstant, notOnOrAfter);

        String logoutRequestXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<saml2p:LogoutRequest\n" +
                "    xmlns:saml2p=\"urn:oasis:names:tc:SAML:2.0:protocol\"\n" +
                "Destination=\"" + LOGOUT_IDP_ENDPOINT + "\" "+
                "ID=\"" + requestId + "\" "+
                "IssueInstant=\"" + issueInstant + "\" "+
                "    NotOnOrAfter=\"" + notOnOrAfter + "\" "+
                "    Reason=\"urn:oasis:names:tc:SAML:2.0:logout:user\"\n" +
                "    Version=\"2.0\">\n\n" +
                "    <saml2:Issuer\n" +
                "        xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">\n" +
                "        " + ISSUER + "\n" +
                "    </saml2:Issuer>\n\n" +
                "    <saml2:NameID\n" +
                "        xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\"\n" +
                "        Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">\n" +
                "  " + samlNameId + "\n" +
                "    </saml2:NameID>\n\n" +
                "    <saml2p:SessionIndex>\n" +
                "       " + samlSessionIndex + "\n" +
                "    </saml2p:SessionIndex>\n" +
                "</saml2p:LogoutRequest>";

        logger.debug("Successfully generated LogoutRequest XML payload");
        return logoutRequestXml;
    }
}

