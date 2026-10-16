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
		logger.info("Initiating Jan-Parichay OAuth 2.0 PKCE authorization flow");

		String codeVerifier = "";
		String codeChallenge = "";
		String state = "";
		String url = "";

		ResourceBundle config = ResourceBundle.getBundle("config");

		try {
			// Generate PKCE Code Verifier & Challenge
			codeVerifier = PKCEUtil.generateCodeVerifier().trim();
			codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier).trim();
			state = UUID.randomUUID().toString().trim();
			
			// Store code_verifier in HTTP session
			request.getSession(true).setAttribute("code_verifier", codeVerifier);

			logger.debug("Generated PKCE challenge (method={}), state={}", 
					config.getString("CODE_CHALLENGE_METHOD"), state);

			// Request Authorization Code (Redirect Call) 
			url = config.getString("AUTHORIZE_URL") + "?response_type="
					+ config.getString("RESPONSE_TYPE") + "&client_id=" + config.getString("CLIENT_ID")
					+ "&redirect_uri=" + URLEncoder.encode(config.getString("REDIRECT_URI"), StandardCharsets.UTF_8)
					+ "&scope=" + config.getString("SCOPE") + "&code_challenge=" + codeChallenge
					+ "&code_challenge_method=" + config.getString("CODE_CHALLENGE_METHOD") + "&state=" + state;

		
			
			
			logger.info("Redirecting client to Jan-Parichay OAuth authorization endpoint: client_id={}, redirect_uri={}",
					config.getString("CLIENT_ID"), config.getString("REDIRECT_URI"));

		} catch (Exception e) {
			logger.error("Error occurred while generating OAuth authorization URL", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"msg\":\"Failed to initiate OAuth flow\"}")
					.build();
		}

		return Response.temporaryRedirect(java.net.URI.create(url)).build();
	}
	
	@POST
	@Path("/refresh")
	@Produces(MediaType.APPLICATION_JSON)
	public Response refresh() {
		logger.info("Received request to refresh OAuth access token");
		try {
			TokenService.refreshToken(request);
			logger.info("OAuth token successfully refreshed for active session");
			return Response.ok("{\"msg\":\"Token refreshed successfully\"}").build();

		} catch (Exception e) {
			logger.error("Failed to refresh OAuth token", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"msg\":\"Token refresh failed\"}")
					.build();
		}
	}
	 
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		logger.info("Received request to revoke OAuth tokens and terminate session");
		try {
			TokenService.revokeToken(request);
			logger.info("OAuth token successfully revoked and session terminated");
			return Response.ok("{\"msg\":\"Logged out successfully\"}").build();

		} catch (Exception e) {
			logger.error("Failed to revoke OAuth token", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"msg\":\"Token revocation failed\"}")
					.build();
		}
	}

}
