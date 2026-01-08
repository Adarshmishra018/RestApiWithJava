package in.nic.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.controller.LoginController;

public class RedisConfig {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	
    // Redis server details
    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;

    // Optional
    public static final int TIMEOUT = 2000; // ms
    public static final int SESSION_EXPIRY = 1800; // 30 minutes

    public RedisConfig() {
        // Prevent object creation
    }
}
