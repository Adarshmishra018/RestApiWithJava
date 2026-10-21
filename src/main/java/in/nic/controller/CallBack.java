package in.nic.controller;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.nic.model.UserDetails;


import in.nic.model.TokenResponse;
import oauth.ParichayOAuthClient;



@WebServlet("/callback")
public class CallBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(CallBack.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Received OAuth callback at /callback");
		try {
			// get code & state from URL query parameters
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			logger.info("OAuth callback parameters received: code present={}, state={}", (code != null), state);

			// get codeVerifier from session
			String codeVerifier = (String) request.getSession().getAttribute("code_verifier");
			logger.debug("Retrieved code_verifier from session");
			
			// Calls request token API & set response in session
			logger.info("Requesting OAuth tokens from provider using authorization code and code_verifier");
			TokenResponse token = ParichayOAuthClient.getToken(code, codeVerifier);
			logger.info("OAuth token exchange completed");
			
			// Set access token & refresh token in session
			request.getSession(false).setAttribute("access_token", token.getAccessToken());
			request.getSession(false).setAttribute("refresh_token", token.getRefreshToken());
			logger.info("Access token and refresh token saved in session");
			
			// Calls user details API & set response in session
			logger.info("Fetching user details from OAuth provider");
			UserDetails user = ParichayOAuthClient.getUserDetails(token.getAccessToken());
			
			
//	//========================================		
//			
//			// new change forGet the COMPLETE raw JSON from Parichay
//				String[] result = ParichayOAuthClient.getUserDetailsWithRawJson(token.getAccessToken());
//				String rawJson = result[0];
//
//				// Parse into your model for individual fields
//				ObjectMapper mapper = new ObjectMapper();
//				UserDetails user = mapper.readValue(rawJson, UserDetails.class);
//
//				// Store BOTH in session
//				request.getSession(false).setAttribute("user_details", user);
//				request.getSession(false).setAttribute("raw_parichay_json", rawJson);  // <-- FULL DATA
//
//
////=========================================

			// Store user details in session
			request.getSession(false).setAttribute("user_details", user);
			logger.info("User details saved in session: userId={}", user != null ? user.getUserId() : "null");

			logger.info("Redirecting user to profile.jsp");
			response.sendRedirect(request.getContextPath() + "/profile.jsp");
			// response.sendRedirect("http://oauthclient.staging.nic.in/profile.jsp");
		} catch(Exception e) {
			logger.error("Exception occurred during OAuth callback processing", e);
		}
	}

}
