package in.nic.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.service.TokenService;

//import session.AuthFilter;
import util.PKCEUtil;

@Path("/login1")
public class AuthController { // that handles OAuth login redirect, 

	private static final Logger logger = LogManager.getLogger(AuthController.class);

	@Context 
	HttpServletRequest request;		//REQUEST	Object created

	
	@GET
	@Path("/loginOAuth")
	public Response loginOAuth() throws NoSuchAlgorithmException {
		logger.debug("loginOAuth class called ");

		// commonly used variables
		String codeVerifier = "";
		String codeChallenge = "";
		String state = "";
		String url = "";

		ResourceBundle config = ResourceBundle.getBundle("config");

		try {
			// Generate PKCE Code Verifier
			codeVerifier = PKCEUtil.generateCodeVerifier();
			
			//create a new session & set code verifier
		 request.getSession(true).setAttribute("code_verifier", codeVerifier);
			
			// Generate PKCE Code challenge
			codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier);
			// Generate state
			state = UUID.randomUUID().toString();
			logger.debug(" code:"+codeVerifier+"/nchallenge:"+codeChallenge+"/nstate:"+state);
			// Request Authorization Code (Redirect Call) 
			url =  config.getString("AUTHORIZE_URL") + "?response_type="
					+ config.getString("RESPONSE_TYPE") + "&client_id=" + config.getString("CLIENT_ID")
					+ "&redirect_uri=" + URLEncoder.encode(config.getString("REDIRECT_URI"), StandardCharsets.UTF_8)
					+ "&scope=" + config.getString("SCOPE") + "&code_challenge=" + codeChallenge
					+ "&code_challenge_method=" + config.getString("CODE_CHALLENGE_METHOD") + "&state=" + state;
		} catch (Exception e) {
			logger.debug("Exception : {} ", e);
		}

		logger.debug("url:" + url);
		return Response.temporaryRedirect(java.net.URI.create(url)).build();// redirects to Jan-Parichay login page
	}
	
	
	 @POST
	    @Path("/refresh")
	    @Produces(MediaType.APPLICATION_JSON)			//returns data in JSON format
	    public Response refresh() {

	        try {
	        	logger.debug("Into refresh ");
	        	 TokenService.refreshToken(request);
	             return Response.ok().build();				//send a response back to the client.
		
	        } catch (Exception e) {
	        	logger.debug("Error: {}", e);
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)//Sets the status code
	                    .entity("{\"msg\":\"Token refresh failed\"}")			//Sets response body JSON as String.
	                    .build();												//creates the Response object
	        }
	    }
	 
	 	@POST
	    @Path("/logout")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response logout() {

	        try {
	        	logger.debug("Into logout");
	        	 TokenService.revokeToken(request);
	             return Response.ok().build();
		
	        } catch (Exception e) {
	        	logger.debug("Error: {}", e);
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("{\"msg\":\"Token refresh failed\"}")
	                    .build();
	        }
	    }

}
