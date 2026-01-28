package util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SamlBase64Util {		// convert SAML XML Base64 encoded string

	private static final Logger logger = LogManager.getLogger(SamlBase64Util.class);
	
    public static String encodeXmlToBase64(String samlXml) {	// XML text to Base64 string is used
    	
        if (samlXml == null || samlXml.isEmpty()) {		//  Prevents encoding of empty XML
        	
            throw new IllegalArgumentException("SAML XML cannot be null or empty");
        }

        //Converts the XML string into raw bytes using UTF-8 encoding
        byte[] xmlBytes = samlXml.getBytes(StandardCharsets.UTF_8);    
       String result= Base64.getEncoder().encodeToString(xmlBytes);		//Converts those bytes into Base64
       logger.debug("base64 {}",result);
        return result;	
    }

    
    
    
    
    
    
    
    
    
    
     //mainly used for SAML HTTP-Redirect Binding
    public static String encodeXmlToBase64UrlEncoded(String samlXml) {				
       String base64 = encodeXmlToBase64(samlXml);			//convert into base64
       logger.debug("bas64 {}",base64);
       return java.net.URLEncoder.encode(base64, StandardCharsets.UTF_8);			//return url encoded
   
}


}
