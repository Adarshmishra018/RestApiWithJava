package in.nic.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.controller.LoginController;

public class ParichayConfig {

	
	private static final Logger logger = LogManager.getLogger(ParichayConfig.class);

    public static final String BASE_URL = "https://janparichaystag.meripehchaan.gov.in";
    		

    public static final String AUTHORIZE_URL = BASE_URL + "/v1/oauth2/authorize";

    public static final String TOKEN_URL = BASE_URL + "/v1/salt/api/oauth2/token";

    public static final String USER_DETAILS_URL = BASE_URL + "/v1/salt/api/oauth2/userdetails";

    public static final String REVOKE_URL =  BASE_URL + "/v1/salt/api/oauth2/revoke";

    
    // Credentials from Jan-Parichay
    public static final String CLIENT_ID = "oauthcliente4l1qijq8cmwsob27gpqf";//"oauthcliente41iqijq8cmwsob27gpqf";
    public static final String CLIENT_SECRET = "2DTALF-m8yxbCOABwfJckMkrfavbFdQg";
    public static final String REDIRECT_URI ="http://oauthclient.staging.nic.in/v1/login1/callback";//"http://localhost:8080/v1/api/login1/callback";//"http://localhost:8080/v1/profile.html";
    		
    //"http://localhost:8080/";
//http://oauthclient.staging.nic.in/callback
    public static final String SCOPE = "user_details";
    public static final String RESPONSE_TYPE = "code";
    public static final String CODE_CHALLENGE_METHOD = "S256";
}
