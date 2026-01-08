package in.nic.controller;

import oauth.*;
//import session.AuthFilter;
import util.PKCEUtil;
import in.nic.model.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Path("/login1")
public class AuthController {			// that handles OAuth login redirect & OAuth callback
	
	private static final Logger logger = LogManager.getLogger(AuthController.class);
	
    private static String codeVerifier;			//Stores the PKCE code_verifier

    @GET
    @Path("/loginOAuth")
    public Response loginOAuth() throws NoSuchAlgorithmException {
    	logger.info("loginOAuth class called ");
        codeVerifier = PKCEUtil.generateCodeVerifier();			//Generate PKCE Code Verifier//random secret string Used to prove authenticity
        String codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);//Derived from codeVerifier & Sent to OAuth provider
        String state = UUID.randomUUID().toString();		//Protects against CSRF attacks & Should ideally be validated in callback
        logger.debug("codeChallenge:"+codeChallenge);
        logger.debug("codeVerifier:"+codeVerifier);
        logger.debug("state:"+state);
        
        String url = ParichayEndpoints.buildAuthorizeUrl(state, codeChallenge); //Authorization URL created
        logger.debug("url:"+url);
        return Response.temporaryRedirect(java.net.URI.create(url)).build();//Browser automatically navigates to OAuth provider
        //Converts a String URL into a URI object.Response object and sends it to the client.
    }
    
    
    
    
   

    @GET
    @Path("/callback")
    public Response callback(@QueryParam("code") String code,@QueryParam("state") String state) {

    	logger.debug("Code & state recieved");
    	logger.debug("Returned code:"+code);
    	logger.debug("Returned state:"+state);
        TokenResponse token =ParichayOAuthClient.getToken(code, codeVerifier);

        return Response.ok(token).build();
    
	}
    
     

}
