package util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.config.RedisConfig;
import in.nic.controller.profileController;
import redis.clients.jedis.Jedis;		//Java client library for Redis.Jedis lets a Java application talk to a Redis server
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	
	private static final Logger logger = LogManager.getLogger(profileController.class);
	    private static JedisPool jedisPool;

	    static {
	        JedisPoolConfig poolConfig = new JedisPoolConfig();
	        poolConfig.setMaxTotal(50);
	        poolConfig.setMaxIdle(10);
	        poolConfig.setMinIdle(2);
	        RedisConfig redisConfig=new RedisConfig();
	        jedisPool = new JedisPool(
	                poolConfig,
	                RedisConfig.REDIS_HOST,
	                RedisConfig.REDIS_PORT,
	                RedisConfig.TIMEOUT
	        );
	    }

	    // Get Redis connection
	    public static Jedis getConnection() {
	        return jedisPool.getResource();
	    }

	    // Close pool on app shutdown
	    public static void shutdown() {
	        if (jedisPool != null) {
	            jedisPool.close();
	        }
	    }
	

    
}
