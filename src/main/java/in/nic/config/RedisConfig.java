package in.nic.config;

import java.io.InputStream;
import java.util.Properties;

public class RedisConfig {
	
//    // Redis server details
//    public static final String REDIS_HOST = "localhost";
//    public static final int REDIS_PORT = 6379;
//
//    // Optional
//    public static final int TIMEOUT = 2000; // ms
//    public static final int SESSION_EXPIRY = 1800; // 30 minutes

	
	static Properties prop = new Properties();

    static {
        try {
            InputStream input = RedisConfig.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            prop.load(input);
            input.close();

        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }
    
	    public static final String REDIS_HOST =
	            prop.getProperty("REDIS_HOST");

	    public static final int REDIS_PORT =
	            Integer.parseInt(prop.getProperty("REDIS_PORT"));

	    public static final int TIMEOUT =
	            Integer.parseInt(prop.getProperty("TIMEOUT"));

	    public static final int SESSION_EXPIRY =
	            Integer.parseInt(prop.getProperty("SESSION_EXPIRY"));
}
