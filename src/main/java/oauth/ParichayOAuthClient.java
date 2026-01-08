package oauth;


import in.nic.config.ParichayConfig;
import in.nic.model.TokenReponse;
//import session.AuthFilter;
import in.nic.model.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Form;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ParichayOAuthClient {			//Hit for Request Token URL

    private static final Client client = ClientBuilder.newClient();			//reusable HTTP client for every request
    private static final Logger logger = LogManager.getLogger(ParichayOAuthClient.class);

    public static TokenReponse getToken(
            String authorizationCode,
            String codeVerifier) {

    	logger.info("Ready for token request");
//        Map<String, String> payload = new HashMap<>();
//        payload.put("client_id", ParichayConfig.CLIENT_ID);
//        payload.put("client_secret", ParichayConfig.CLIENT_SECRET);
//        payload.put("redirect_uri", ParichayConfig.REDIRECT_URI);
//        payload.put("grant_type", "authorization_code");
//        payload.put("code", authorizationCode);
//        payload.put("code_verifier", codeVerifier);

//        Response response = client
//                .target(ParichayConfig.TOKEN_URL)
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.json(payload));
    	
    	Form form = new Form();												//Create Form body
        form.param("client_id", ParichayConfig.CLIENT_ID);					//Add required OAuth parameters		
        form.param("client_secret", ParichayConfig.CLIENT_SECRET);
        form.param("redirect_uri", ParichayConfig.REDIRECT_URI);
        form.param("grant_type", "authorization_code");
        form.param("code", authorizationCode);
        form.param("code_verifier", codeVerifier);			//PKCE security check,Parichay hashes this and matches it with code_challenge
    	
        
        //Points to Jan Parichay token endpoint, expect JSON in response,ends POST request with:Form data,Correct Content-Type
    	Response response = client
                .target(ParichayConfig.TOKEN_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));

        if (response.getStatus() != 200) {					//Handle failure response
            String error = response.readEntity(String.class);
            logger.error("Token request failed: {}", error);
            throw new RuntimeException("Token request failed");
        }

        return response.readEntity(TokenReponse.class);			//read token response
    }
}
