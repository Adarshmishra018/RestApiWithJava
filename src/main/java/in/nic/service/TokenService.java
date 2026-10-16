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

	// REFRESH SERVICE
    public static RefreshTokenResponse refreshToken(HttpServletRequest request) {
        logger.info("Executing OAuth token refresh service");
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("Token refresh rejected: No active HTTP session found");
            throw new RuntimeException("Session not found");
        }

        String refreshToken = (String) session.getAttribute("refresh_token");
        if (refreshToken == null || refreshToken.isBlank()) {
            logger.warn("Token refresh rejected: No refresh_token attribute stored in session {}", session.getId());
            throw new RuntimeException("Refresh token not found in session");
        }

        logger.debug("Found refresh token in session. Calling OAuth provider...");
        RefreshTokenResponse newToken = ParichayOAuthClient.refreshAccessToken(refreshToken);

        if (newToken == null || newToken.getAccessToken() == null) {
            logger.error("OAuth provider returned empty token response during refresh");
            throw new RuntimeException("Failed to obtain new access token from provider");
        }

        // Set new access & refresh token in session
        session.setAttribute("access_token", newToken.getAccessToken());
        if (newToken.getRefreshToken() != null && !newToken.getRefreshToken().isBlank()) {
            session.setAttribute("refresh_token", newToken.getRefreshToken());
        }

        logger.info("Session successfully updated with renewed access token. Expiry: {}", newToken.getExpiresIn());
        return newToken;
    }
    
    // REVOKE SERVICE
    public static RevokeTokenResponse revokeToken(HttpServletRequest request) {
        logger.info("Executing OAuth token revocation service");
        HttpSession session = request.getSession(false);
       
        if (session == null) {
            logger.warn("Token revocation skipped: No active HTTP session found");
            throw new RuntimeException("Session not found");
        }

        String accessToken = (String) session.getAttribute("access_token");
        if (accessToken == null || accessToken.isBlank()) {
            logger.warn("No active access token found in session {} to revoke. Invalidating session directly.", session.getId());
            session.invalidate();
            return null;
        }

        logger.info("Calling OAuth provider to revoke active access token for session {}", session.getId());
        RevokeTokenResponse logout = ParichayOAuthClient.revokeAccessToken(accessToken);

        session.invalidate();
        logger.info("HTTP session successfully invalidated following token revocation");

        return logout;    
    }
}

