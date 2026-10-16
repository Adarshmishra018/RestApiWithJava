package util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PKCEUtil {

    private static final Logger logger = LogManager.getLogger(PKCEUtil.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code);
        logger.debug("Generated 32-byte random PKCE code_verifier (length={})", verifier.length());
        return verifier;
    }

    public static String generateCodeChallenge(String codeVerifier)
            throws NoSuchAlgorithmException {
        if (codeVerifier == null || codeVerifier.isBlank()) {
            logger.error("Cannot generate PKCE code_challenge: code_verifier is null or empty");
            throw new IllegalArgumentException("codeVerifier cannot be null or empty");
        }

        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(bytes);

        String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        logger.debug("Computed SHA-256 S256 PKCE code_challenge (length={})", challenge.length());
        return challenge;
    }
}
