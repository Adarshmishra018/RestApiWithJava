package in.nic.config;

public class RedisConfig {
	
    // Redis server details
    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;

    // Optional
    public static final int TIMEOUT = 2000; // ms
    public static final int SESSION_EXPIRY = 1800; // 30 minutes

}
