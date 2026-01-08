package oauth;


import in.nic.config.*;
import in.nic.controller.profileController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParichayEndpoints {			//request for code

	private static final Logger logger = LogManager.getLogger(ParichayEndpoints.class);
	
    public static String buildAuthorizeUrl(
            String state,
            String codeChallenge) {

    	logger.info("Authorization URL created");
    	
        return ParichayConfig.AUTHORIZE_URL +
                "?response_type=" + ParichayConfig.RESPONSE_TYPE +
                "&client_id=" + ParichayConfig.CLIENT_ID +
                "&redirect_uri=" + encode(ParichayConfig.REDIRECT_URI) +
                "&scope=" + ParichayConfig.SCOPE +
                "&code_challenge=" + codeChallenge +
                "&code_challenge_method=" + ParichayConfig.CODE_CHALLENGE_METHOD +
                "&state=" + state;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
