package in.nic.controller;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import in.nic.model.UserDetails;


import in.nic.model.TokenResponse;
import oauth.ParichayOAuthClient;



@WebServlet("/callback")
public class CallBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(CallBack.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
		//get code & state from URL query parameters
		String code = request.getParameter("code");
		String state = request.getParameter("state");

		//get codeVerifier from session
		String codeVerifier = (String) request.getSession().getAttribute("code_verifier");
		logger.debug(" code:"+code+"\nstate:"+state);
		
        // Calls request token API & set response in session
        TokenResponse token = ParichayOAuthClient.getToken(code, codeVerifier);
        logger.debug("Token Fetched: {}",token);
        
        //Set access token & refresh token in session
        request.getSession(false).setAttribute("access_token", token.getAccessToken());
        request.getSession(false).setAttribute("refresh_token", token.getRefreshToken());
        
        // Calls user details API & set response in session
        UserDetails user = ParichayOAuthClient.getUserDetails(token.getAccessToken());
        // Store user details in session
        request.getSession(false).setAttribute("user_details", user);
        logger.debug("user details123 {}: ",user);

        response.sendRedirect(request.getContextPath() + "/profile.jsp");
       // response.sendRedirect("http://oauthclient.staging.nic.in/profile.jsp");
		}catch(Exception e) {
			 logger.debug("Exception occurred {}: ",e);
		}
	}

}
