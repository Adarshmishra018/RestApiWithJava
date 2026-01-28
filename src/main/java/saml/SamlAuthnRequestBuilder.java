package saml;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SamlAuthnRequestBuilder {

	private static final Logger logger = LogManager.getLogger(SamlAuthnRequestBuilder.class);
	
	//client name
    private static final String ISSUER = "samljpclient";
    
    // callback url
    private static final String ACS_URL =
            "http://jpmeripehchaan.staging.nic.in/saml/home";

    private static final String DESTINATION =
            "https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogin";

    //redirect url
    private static final String LOGOUT_IDP_ENDPOINT = 
    		"https://janparichaystag.meripehchaan.gov.in/v1/SAMLLogout";
 
    //building xml for login request
    public static String build() {
    	
        //Random  Unique ID 
        String requestId = "_" + UUID.randomUUID();
        logger.debug( "requestId: {}",requestId);
        
        // fetch time of IssueInstant
        String issueInstant = Instant.now().toString();
        logger.debug( "issueInstant: {}",issueInstant);
    

//        ZonedDateTime istTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
//        logger.debug( "istTime: {}",istTime);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
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
    }
    
    //building xml for logout request
    
    public static String build2(HttpServletRequest request) {		

    	HttpSession session = request.getSession(false); //get session request
        //Random  Unique ID 
        String requestId = "_" + UUID.randomUUID();
        logger.debug( "requestId: {}",requestId);
        
        // fetch time of IssueInstant
        String issueInstant = Instant.now().toString();
        logger.debug( "issueInstant: {}",issueInstant);
    
      String samlNameId = (String) session.getAttribute("SAML_NAME_ID");// get name id from session and covert in string
        logger.debug( "samlNameId: {}",samlNameId);
       
        String samlSessionIndex =(String) session.getAttribute("SAML_SESSION_INDEX");// get session index from session and covert in string
        logger.debug( "samlSessionIndex: {}",samlSessionIndex);
        
        String notOnOrAfter =session.getAttribute("notOnOrAfter").toString();// get notOnOrAfter from session 
      
		/*
		 * ZonedDateTime istTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		 * logger.debug( "istTime: {}",istTime);
		 */
        
        String logoutRequestXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<saml2p:LogoutRequest\n" +
                "    xmlns:saml2p=\"urn:oasis:names:tc:SAML:2.0:protocol\"\n" +
                "Destination=\"" + LOGOUT_IDP_ENDPOINT + "\" "+
                "ID=\"" + requestId + "\" "+
                 "IssueInstant=\"" + issueInstant + "\" "+
                "    NotOnOrAfter=\""+notOnOrAfter+ "\" "+
                "    Reason=\"urn:oasis:names:tc:SAML:2.0:logout:user\"\n" +
                "    Version=\"2.0\">\n\n" +

                "    <saml2:Issuer\n" +
                "        xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">\n" +
                "        samljpclient\n" +
                "    </saml2:Issuer>\n\n" +

                "    <saml2:NameID\n" +
                "        xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\"\n" +
                "        Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">\n" +
                 "  "+samlNameId+"\n" +
                "    </saml2:NameID>\n\n" +

                "    <saml2p:SessionIndex>\n" +
                "       "+samlSessionIndex+"\n" +
                "    </saml2p:SessionIndex>\n" +

                "</saml2p:LogoutRequest>";
        	return logoutRequestXml;
    }
}

