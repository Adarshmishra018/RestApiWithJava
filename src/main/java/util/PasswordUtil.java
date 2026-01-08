package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.controller.profileController;

public class PasswordUtil {

	private static final Logger logger = LogManager.getLogger(profileController.class);
	
    // SHA-256 hashing method
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(
                password.getBytes(StandardCharsets.UTF_8)//Converts password to UTF-8 bytes[]
            );

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {			//Converts raw bytes into readable hexadecimal string
                hexString.append(String.format("%02x", b));		//Each byte into 2 hex characters
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}