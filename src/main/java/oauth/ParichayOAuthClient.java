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

    
    
    // ACCESS TOKEN REQUEST BODY & URL HIT
    public static TokenResponse getToken(String authorizationCode, String codeVerifier) {
        ResourceBundle config = ResourceBundle.getBundle("config");
        String tokenUrl = config.getString("TOKEN_URL");
        String clientId = config.getString("CLIENT_ID");

        logger.info("Sending Authorization Code exchange request to OAuth Token URL: {}", tokenUrl);
        logger.debug("OAuth Token request parameters: client_id={}, grant_type=authorization_code, redirect_uri={}",
                clientId, config.getString("REDIRECT_URI"));

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType("authorization_code");
        tokenRequest.setClientId(clientId);
        tokenRequest.setClientSecret(config.getString("CLIENT_SECRET"));
        tokenRequest.setRedirectUri(config.getString("REDIRECT_URI"));
        tokenRequest.setCode(authorizationCode);
        tokenRequest.setCodeVerifier(codeVerifier);

        Response response = client
                .target(tokenUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(tokenRequest, MediaType.APPLICATION_JSON));

        int statusCode = response.getStatus();
        logger.info("OAuth Token endpoint responded with HTTP status: {}", statusCode);

        if (statusCode != 200) {
            String error = response.readEntity(String.class);
            logger.error("OAuth Token request failed with status {}: responseBody={}", statusCode, error);
            throw new RuntimeException("Token request failed with status " + statusCode);
        }

        TokenResponse tokenResponse = response.readEntity(TokenResponse.class);
        logger.info("OAuth tokens issued successfully: token_type={}, expires_in={}",
                tokenResponse.getTokenType(), tokenResponse.getExpiresIn());

        return tokenResponse;
    }

    // USER DETAILS URL HIT
    public static UserDetails getUserDetails(String accessToken) {
        ResourceBundle config = ResourceBundle.getBundle("config");
        String userDetailsUrl = config.getString("USER_DETAILS_URL");

        logger.info("Requesting user profile from OAuth UserDetails endpoint: {}", userDetailsUrl);

        Response response = client
                .target(userDetailsUrl)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .get();

        int statusCode = response.getStatus();
        logger.info("OAuth UserDetails endpoint responded with HTTP status: {}", statusCode);

        if (statusCode != 200) {
            String error = response.readEntity(String.class);
            logger.error("Failed to fetch user details from provider. HTTP status={}: responseBody={}", statusCode, error);
            throw new RuntimeException("Failed to fetch user details: HTTP " + statusCode);
        }

        UserDetails user = response.readEntity(UserDetails.class);
        logger.info("Successfully fetched user profile: userId={}, name={} {}", 
                user.getUserId(), user.getFirstName(), user.getLastName());

        return user;
    }

    // REFRESH TOKEN URL HIT
    public static RefreshTokenResponse refreshAccessToken(String refreshToken) {
        RefreshTokenResponse rfr = null;
        ResourceBundle config = ResourceBundle.getBundle("config");
        String refreshUrl = config.getString("REFRESH_URL");

        logger.info("Calling OAuth Refresh Token API at: {}", refreshUrl);

        String requestBody = "{\"grant_type\":\"refresh_token\"}";

        try {
            Response response = client.target(refreshUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, refreshToken)
                    .post(Entity.json(requestBody));

            int statusCode = response.getStatus();
            logger.info("OAuth Refresh Token API responded with HTTP status: {}", statusCode);

            if (statusCode != 200) {
                String error = response.readEntity(String.class);
                logger.error("OAuth token refresh failed with HTTP {}: responseBody={}", statusCode, error);
                throw new RuntimeException("Refresh token failed with status " + statusCode);
            }

            rfr = response.readEntity(RefreshTokenResponse.class);
            logger.info("OAuth token successfully refreshed by authorization server");

        } catch (Exception e) {
            logger.error("Exception occurred while communicating with OAuth Refresh Token endpoint", e);
            throw e;
        }

        return rfr;
    }

    // REVOKE TOKEN URL HIT
    public static RevokeTokenResponse revokeAccessToken(String accessToken) {
        RevokeTokenResponse rfr = null;
        ResourceBundle config = ResourceBundle.getBundle("config");
        String revokeUrl = config.getString("REVOKE_URL");

        logger.info("Calling OAuth Revoke Token API at: {}", revokeUrl);

        try {
            Response response = client.target(revokeUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .get();

            int statusCode = response.getStatus();
            logger.info("OAuth Revoke Token API responded with HTTP status: {}", statusCode);

            if (statusCode == 200) {
                rfr = response.readEntity(RevokeTokenResponse.class);
                logger.info("OAuth token revocation successful: status={}", rfr != null ? rfr.getStatus() : "OK");
            } else {
                logger.warn("OAuth token revocation responded with non-200 status: {}", statusCode);
            }

        } catch (Exception e) {
            logger.error("Exception occurred while communicating with OAuth Revoke Token endpoint", e);
            throw e;
        }

        return rfr;
    } 
  
  
  
}
