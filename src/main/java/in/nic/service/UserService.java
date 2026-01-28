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
	UserRepository userRepo=new UserRepository();
	public boolean registerUser(UserModel userModel) {//check if already exis
		if (userRepo.existsByEmail(userModel.getEmail())) {
			 return true; //found
        }
		 userRepo.save(userModel);			//insert new record
		 return false; //not found
	}

	
// using redis wit db 
	public UserModel getUser(String email, String password) {

	    String redisKey = "user:" + email;

	    //  Try Redis first
	    long redisStart = System.currentTimeMillis();

	    try (Jedis jedis = RedisUtil.getConnection()) {

	        Map<String, String> cachedUser = jedis.hgetAll(redisKey);

	        // If Redis has user details
	        if (cachedUser != null && !cachedUser.isEmpty()) {

	            long redisEnd = System.currentTimeMillis();
	            logger.debug("User fetched from Redis in "+ (redisEnd - redisStart) + " ms");

	            // if found set UserModel
	            UserModel user = new UserModel();
	            user.setId(cachedUser.get("id"));
	            user.setFullName(cachedUser.get("name"));
	            user.setEmail(cachedUser.get("email"));
	            user.setPassword(cachedUser.get("password")); // hashed
	            user.setMobile(cachedUser.get("mobile"));

	            // Match password
	            if (user.getPassword().equals(password)) {
	                return user;
	            } else {
	                return null;
	            }
	        }
	    }

	    // If not in redis Fetch from DB 
	    long dbStart = System.currentTimeMillis();

	    UserModel user = userRepo.findByEmail(email, password);		//userModel got details

	    long dbEnd = System.currentTimeMillis();
	    logger.debug("User fetched from DB in "+ (dbEnd - dbStart) + " ms");
	    	logger.debug(" value from mongoDB DataBase ",user);
	   
	    // Now Store in Redis from userModel 
	    if (user != null) {
	        try (Jedis jedis = RedisUtil.getConnection()) {

	            Map<String, String> userMap = new HashMap<>();
	            if (user.getId() != null)	//
	                userMap.put("id", user.getId()); 
	            if (user.getFullName() != null)
	                userMap.put("name", user.getFullName());
	            if (user.getEmail() != null)
	                userMap.put("email", user.getEmail());
	            if (user.getPassword() != null)
	                userMap.put("password", user.getPassword());
	            if (user.getMobile() != null)
	                userMap.put("mobile", user.getMobile());
	            if (!userMap.isEmpty()) {
	                jedis.hset(redisKey, userMap);			//used to store hash data in Redis
	                jedis.expire(redisKey, RedisConfig.SESSION_EXPIRY);//expire sets a time-to-live (TTL) for Redis.
	            }            
	        }
	    }
	    return user;
	}


	
	
//UPDATE USER DETAILS
	public boolean updateProfile(UserModel user) {
	    return userRepo.updateUser(user);
	}

//
	public boolean resetPassword(String email, String password) {
	    return userRepo.updatePassword(email, password);
	}
} 
	

