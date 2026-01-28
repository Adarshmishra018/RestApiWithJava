package in.nic.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.model.RefreshTokenResponse;
import in.nic.model.RevokeTokenResponse;
import oauth.ParichayOAuthClient;

public class TokenService {
	private static final Logger logger = LogManager.getLogger(TokenService.class);

	//REFRESH SERVICE
    public static RefreshTokenResponse refreshToken(HttpServletRequest request) {

	    logger.debug("Inside refresh token service ")	;
	    HttpSession session = request.getSession(false);
      if (session == null) {				
            throw new RuntimeException("Session not found");
        }

        String refreshToken =(String) session.getAttribute("refresh_token");//get refresh token from session

        
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token not found");
        }

         //Call OAuth refresh token API & return response to AUthController
        RefreshTokenResponse newToken =ParichayOAuthClient.refreshAccessToken(refreshToken);
        
        logger.debug("Print new token: {}",newToken); //refresh token response

        // Set new access & Refresh in session
        session.setAttribute("access_token", newToken.getAccessToken());
        session.setAttribute("refresh_token", newToken.getRefreshToken());

         
        return newToken;
    }
    
    
    
    //REVOKE SERVICE
    public static RevokeTokenResponse revokeToken(HttpServletRequest request) {
    	 
    	logger.debug("Inside revoke token service")	;
        HttpSession session = request.getSession(false);
        logger.debug("session",session)	;
       
            if (session == null) {					
                throw new RuntimeException("Session not found");		//throws exception to calling class if session is null
            }

            String accessToken =(String) session.getAttribute("access_token");  //fetch accesss token from session in token type obj
//           TokenResponse token =(TokenResponse) session.getAttribute("access_token");  //fetch accesss token from session in token type obj
           
//            String accessToken = token.getAccessToken();//extract the actual token string
           
            logger.debug("Print session accessToken: {}",accessToken);

            if (accessToken == null || accessToken.isBlank()) {		//throws exception to calling class if access token is null
                throw new RuntimeException("Refresh token not found");
            }
            //Call OAuth revoke token API & return response to AuthController
            RevokeTokenResponse logout = ParichayOAuthClient.revokeAccessToken(accessToken);
            
            logger.debug("Print logout response: {}",logout);
           
            if (session != null) {
                session.invalidate();//delete session
               
            }
            
            return logout;    
    }
}

