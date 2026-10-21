package in.nic.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ParichayConfig {

	
	private static final Logger logger = LogManager.getLogger(ParichayConfig.class);

//    public static final String BASE_URL = "https://janparichaystag.meripehchaan.gov.in";
//    		
//
//    public static final String AUTHORIZE_URL = BASE_URL + "/v1/oauth2/authorize";
//
//    public static final String TOKEN_URL = BASE_URL + "/v1/salt/api/oauth2/token";
//
//    public static final String USER_DETAILS_URL = BASE_URL + "/v1/salt/api/oauth2/userdetails";
//
//    public static final String REVOKE_URL =  BASE_URL + "/v1/salt/api/oauth2/revoke";
//
//    
//    // Credentials from Jan-Parichay
//    public static final String CLIENT_ID = "oauthcliente4l1qijq8cmwsob27gpqf";//"oauthcliente41iqijq8cmwsob27gpqf";
//    public static final String CLIENT_SECRET = "2DTALF-m8yxbCOABwfJckMkrfavbFdQg";
//    public static final String REDIRECT_URI ="http://oauthclient.staging.nic.in/v1/login1/callback";//"http://localhost:8080/v1/api/login1/callback";//"http://localhost:8080/v1/profile.html";
//   
//    public static final String SCOPE = "user_details";
//    public static final String RESPONSE_TYPE = "code";
//    public static final String CODE_CHALLENGE_METHOD = "S256";
    
    


	    private static final Properties properties = new Properties();

	    static {
	        try (InputStream input =
	        		ParichayConfig.class.getClassLoader()
	                    .getResourceAsStream("config.properties")) {

	            if (input == null) {
	                throw new RuntimeException("config.properties not found");
	            }

	            properties.load(input);

	        } catch (Exception e) {
	            throw new RuntimeException("Failed to load config.properties", e);
	        }
	    }
    
    public static final String BASE_URL =
            properties.getProperty("BASE_URL");

    public static final String AUTHORIZE_URL =
            BASE_URL + properties.getProperty("AUTHORIZE_URL");

    public static final String TOKEN_URL =
            BASE_URL + properties.getProperty("TOKEN_URL");

    public static final String USER_DETAILS_URL =
            BASE_URL + properties.getProperty("USER_DETAILS_URL");

    public static final String REVOKE_URL =
            BASE_URL + properties.getProperty("REVOKE_URL");

    public static final String CLIENT_ID =
            properties.getProperty("CLIENT_ID");

    public static final String CLIENT_SECRET =
            properties.getProperty("CLIENT_SECRET");

    public static final String REDIRECT_URI =
            properties.getProperty("REDIRECT_URI");

    public static final String SCOPE =
            properties.getProperty("SCOPE");

    public static final String RESPONSE_TYPE =
            properties.getProperty("RESPONSE_TYPE");

    public static final String CODE_CHALLENGE_METHOD =
            properties.getProperty("CODE_CHALLENGE_METHOD");
}
