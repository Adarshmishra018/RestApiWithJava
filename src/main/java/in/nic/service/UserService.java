package in.nic.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.config.RedisConfig;
import in.nic.model.UserModel;
import in.nic.repository.UserRepository;
import redis.clients.jedis.Jedis;
import util.RedisUtil;


public class UserService {
	private static final Logger logger = LogManager.getLogger(UserService.class);
	private final UserRepository userRepo = new UserRepository();

	public boolean registerUser(UserModel userModel) {
		logger.info("Checking if user already exists with email: {}", userModel.getEmail());
		if (userRepo.existsByEmail(userModel.getEmail())) {
			logger.warn("User already exists in repository with email: {}", userModel.getEmail());
			return true;
		}
		logger.info("Saving new user to database: email={}", userModel.getEmail());
		userRepo.save(userModel);
		return false;
	}

	// Fetch user using Redis with DB fallback
	public UserModel getUser(String email, String password) {
	    String redisKey = "user:" + email;
	    long redisStart = System.currentTimeMillis();

	    // 1. Try Redis cache first
	    try (Jedis jedis = RedisUtil.getConnection()) {
	        Map<String, String> cachedUser = jedis.hgetAll(redisKey);

	        if (cachedUser != null && !cachedUser.isEmpty()) {
	            long redisEnd = System.currentTimeMillis();
	            logger.info("Cache HIT: Retrieved user '{}' from Redis in {} ms", email, (redisEnd - redisStart));

	            UserModel user = new UserModel();
	            user.setId(cachedUser.get("id"));
	            user.setFullName(cachedUser.get("name"));
	            user.setEmail(cachedUser.get("email"));
	            user.setPassword(cachedUser.get("password"));
	            user.setMobile(cachedUser.get("mobile"));

	            if (user.getPassword() != null && user.getPassword().equals(password)) {
	                logger.info("Password matched for cached user: email={}", email);
	                return user;
	            } else {
	                logger.warn("Password mismatch for cached user: email={}", email);
	                return null;
	            }
	        } else {
	            logger.info("Cache MISS: User '{}' not found in Redis cache", email);
	        }
	    } catch (Exception e) {
	        logger.error("Redis error while fetching user '{}'. Falling back to DB.", email, e);
	    }

	    // 2. Fetch from MongoDB database
	    long dbStart = System.currentTimeMillis();
	    logger.info("Querying MongoDB database for user: email={}", email);
	    UserModel user = userRepo.findByEmail(email, password);
	    long dbEnd = System.currentTimeMillis();

	    if (user != null) {
	        logger.info("DB HIT: Retrieved user '{}' from MongoDB in {} ms", email, (dbEnd - dbStart));

	        // 3. Populate Redis cache
	        try (Jedis jedis = RedisUtil.getConnection()) {
	            Map<String, String> userMap = new HashMap<>();
	            if (user.getId() != null) userMap.put("id", user.getId());
	            if (user.getFullName() != null) userMap.put("name", user.getFullName());
	            if (user.getEmail() != null) userMap.put("email", user.getEmail());
	            if (user.getPassword() != null) userMap.put("password", user.getPassword());
	            if (user.getMobile() != null) userMap.put("mobile", user.getMobile());

	            if (!userMap.isEmpty()) {
	                jedis.hset(redisKey, userMap);
	                jedis.expire(redisKey, RedisConfig.SESSION_EXPIRY);
	                logger.info("Cached user '{}' in Redis with TTL={} seconds", email, RedisConfig.SESSION_EXPIRY);
	            }
	        } catch (Exception e) {
	            logger.error("Failed to cache user '{}' in Redis", email, e);
	        }
	    } else {
	        logger.warn("DB MISS: No matching user found in MongoDB for email '{}'", email);
	    }

	    return user;
	}

	// UPDATE USER DETAILS
	public boolean updateProfile(UserModel user) {
	    logger.info("Executing profile update for userId: {}", user.getId());
	    boolean updated = userRepo.updateUser(user);

	    if (updated && user.getEmail() != null) {
	        // Invalidate stale cache
	        try (Jedis jedis = RedisUtil.getConnection()) {
	            jedis.del("user:" + user.getEmail());
	            logger.info("Invalidated Redis cache for updated user: email={}", user.getEmail());
	        } catch (Exception e) {
	            logger.error("Failed to invalidate Redis cache after profile update for: {}", user.getEmail(), e);
	        }
	    }
	    return updated;
	}

	// RESET PASSWORD
	public boolean resetPassword(String email, String password) {
	    logger.info("Executing password reset for email: {}", email);
	    boolean updated = userRepo.updatePassword(email, password);

	    if (updated) {
	        // Invalidate stale cache
	        try (Jedis jedis = RedisUtil.getConnection()) {
	            jedis.del("user:" + email);
	            logger.info("Invalidated Redis cache after password reset for: {}", email);
	        } catch (Exception e) {
	            logger.error("Failed to invalidate Redis cache after password reset for: {}", email, e);
	        }
	    }
	    return updated;
	}
} 
	

