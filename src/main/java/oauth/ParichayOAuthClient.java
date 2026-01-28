package oauth;

import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.model.RefreshTokenResponse;
import in.nic.model.RevokeTokenResponse;
import in.nic.model.TokenRequest;
//import session.AuthFilter;
import in.nic.model.TokenResponse;
import in.nic.model.UserDetails;

public class ParichayOAuthClient {			//Hit for Request Token URL

    private static final Client client = ClientBuilder.newClient();			//reusable HTTP client to make outgoing REST calls
    private static final Logger logger = LogManager.getLogger(ParichayOAuthClient.class);

    
    
    //ACCESS TOKEN REQUEST BODY & URL HIT
    public static TokenResponse getToken(String authorizationCode,String codeVerifier) {		//performs Authorization Code to Access Token exchange.
            

    	logger.info(" Token request started: AuthCode:{} | CodeVerifier:{}",authorizationCode, codeVerifier);
    	
    	ResourceBundle config = ResourceBundle.getBundle("config");
    		
    	//building request object for token exchange
    	TokenRequest tokenRequest = new TokenRequest();     //object created and set values
        tokenRequest.setGrantType("authorization_code");
        tokenRequest.setClientId(config.getString("CLIENT_ID"));
        tokenRequest.setClientSecret(config.getString("CLIENT_SECRET"));
        tokenRequest.setRedirectUri(config.getString("REDIRECT_URI"));
        tokenRequest.setCode(authorizationCode);
        tokenRequest.setCodeVerifier(codeVerifier);
        
        logger.debug("Request body: {}",tokenRequest);
        logger.debug("Entity:{} ",Entity.entity(tokenRequest, MediaType.APPLICATION_JSON));
        
        //calls request token Api and send response to Callback controller
        Response response = client
                .target(config.getString("TOKEN_URL"))
                .request(MediaType.APPLICATION_JSON)   // expect JSON back
                .post(Entity.entity(tokenRequest, MediaType.APPLICATION_JSON)); //Sends tokenRequest as JSON  
        //Takes the Java object tokenRequest, convert it to JSON, and send it as the request body

        logger.debug("Token response status={}",response);
        
        if (response.getStatus() != 200) {					//Handle failure response
            String error = response.readEntity(String.class);
            logger.error("Token request failed: {}", error);
            throw new RuntimeException("Token request failed");
        }

        logger.info("Token issued successfully{} ");//,response.readEntity(TokenResponse.class));
        
        return response.readEntity(TokenResponse.class);			//read token response
        //Read the HTTP response body, convert the JSON into a TokenResponse Java object, and return it.
    }
    
    
  //USER DETAILS URL HIT
    public static UserDetails getUserDetails(String accessToken) {

    	ResourceBundle config = ResourceBundle.getBundle("config");
      //  Client client = ClientBuilder.newClient();			//reusable HTTP client for every request
        Response response = client
                .target(config.getString("USER_DETAILS_URL"))
                .request(MediaType.APPLICATION_JSON)			// expect JSON back
                .header(HttpHeaders.AUTHORIZATION, accessToken) // Set access_token in header 
                .get();											//Http Get request

        logger.debug("acess token {} ", accessToken);
        if (response.getStatus() != 200) {						
            String error = response.readEntity(String.class);//read the response body and convert it into String.
            logger.error("User details fetch failed: {}", error);
            throw new RuntimeException("Failed to fetch user details");//throws Exception to the calling class
        }
        
        UserDetails user=response.readEntity(UserDetails.class);//read the response body & convert it into UserDetails
        logger.debug("User details {} ", user);

        return  user;
    }
      
    
   
    //REFRESH TOKEN URL HIT
    public static RefreshTokenResponse refreshAccessToken(String refreshToken) {

		  RefreshTokenResponse rfr = null;
		  ResourceBundle config = ResourceBundle.getBundle("config");	//
	        
	      String requestBody = "{\"grant_type\":\"refresh_token\"}";		//JSON request body to send for refresh token

	        logger.info("Calling Refresh Token API, refrest_token:{}",refreshToken);

	        try {
	        	//calls request refresh token Api and send response to Auth controller
				Response response = client.target(config.getString("REFRESH_URL"))
											.request(MediaType.APPLICATION_JSON)// expect back JSON
											.header(HttpHeaders.AUTHORIZATION, refreshToken) // Set refresh_token in header  
																								
											.post(Entity.json(requestBody)); // POST type method with request body

				logger.debug("Refresh token response status: {}", response.getStatus());

				//check if response is unsuccessful
			if (response.getStatus() != 200) {				//get the status code
	            String error = response.readEntity(String.class);		//Read response and convert in string
	            logger.error("Refresh token failed: {}", error);
	            throw new RuntimeException("Refresh token failed");
	        }

				// Reads response body & Converts it to a RefreshTokenResponse Java object.
				rfr = response.readEntity(RefreshTokenResponse.class);// to map the response into RefreshTokenResponse 
	        } catch(Exception e) {
	        	logger.debug("Refresh token error: {}", e);
	        }
	        
	        
	        logger.debug("Refresh token response status: {}", rfr);

	        return rfr;
	    }
    
    
    //REVOKE TOKEN URL HIT
  public static RevokeTokenResponse revokeAccessToken(String accessToken) {
    	
        RevokeTokenResponse rfr = null;
        ResourceBundle config = ResourceBundle.getBundle("config");
        try {
        	logger.info("Calling Revoke Token API");
        	
        	//calls request revoke token Api and send response to Auth controller
			Response response = client.target(config.getString("REVOKE_URL"))
					.request(MediaType.APPLICATION_JSON)// expect back JSON
					.header(HttpHeaders.AUTHORIZATION, accessToken)// Set access_token in header
					.get();											 // Get Method

			logger.debug("Revoke token response status: {}", response.getStatus());
			rfr = response.readEntity(RevokeTokenResponse.class);		//to read and convert the response into RefreshTokenResponse
        } catch(Exception e) {
        	logger.debug("Revoke token error: {}", e);
        }
        
        
        logger.debug("Revoke token response status: {}", rfr);

        return rfr;
    } 
  
  
  
}
