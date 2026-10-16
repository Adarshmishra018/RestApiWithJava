package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	
    private static final Logger logger = LogManager.getLogger(RedisUtil.class);
    private static JedisPool jedisPool;

    static {
        try {
            logger.info("Initializing JedisPool for Redis: host={}, port={}, timeout={}ms, maxTotal=50",
                    RedisConfig.REDIS_HOST, RedisConfig.REDIS_PORT, RedisConfig.TIMEOUT);

            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(50);
            poolConfig.setMaxIdle(10);
            poolConfig.setMinIdle(2);
            jedisPool = new JedisPool(
                    poolConfig,
                    RedisConfig.REDIS_HOST,
                    RedisConfig.REDIS_PORT,
                    RedisConfig.TIMEOUT
            );
            logger.info("JedisPool initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize JedisPool for Redis", e);
        }
    }

    // Get Redis connection from pool
    public static Jedis getConnection() {
        return jedisPool.getResource();
    }

    // Close pool on app shutdown
    public static void shutdown() {
        if (jedisPool != null) {
            logger.info("Closing JedisPool connection pool");
            jedisPool.close();
            logger.info("JedisPool connection pool closed successfully");
        }
    }
}
