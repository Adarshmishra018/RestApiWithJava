package in.nic.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.controller.LoginController;
import redis.clients.jedis.Jedis;
import util.RedisUtil;

public class SessionService {

	@Context 							// injects HTTP request object in Jersey resource
	HttpServletRequest request; 

	@Context 								
	HttpServletResponse response; // outgoing HTTP response

	
	String sessionKey=null;
	private static final Logger logger = LogManager.getLogger(LoginController.class);
	public void createRedisSession(String sessionId) {
		
		sessionKey = "session:" + sessionId;
		try (Jedis jedis = RedisUtil.getConnection()) { // returns a Jedis connection
			jedis.setex( // SET a key with a value and EXPIRE it automatically after given time.
					"session:" + sessionId, 1800, // 30 minutes
					sessionId);

			logger.info("Session ID stored in Redis" + sessionId);
		}
	}

	public void createCookieSession(String sessionId,HttpServletResponse response) {
		Cookie cookie = new Cookie("SESSION_ID", sessionId);
		cookie.setHttpOnly(true); // protection from XSS
		cookie.setPath("/"); // Cookie is sent for all URL
		cookie.setMaxAge(1800); // cookie expires after 30 mins
		response.addCookie(cookie); // Send cookie to browser
		logger.info("Session ID stored in cookie" + sessionId);	
	}


	public void deleteRedisSession(String sessionId) {
		// 3️.Delete session from Redis
				if (sessionId != null) {
					try (Jedis jedis = RedisUtil.getConnection()) {
						jedis.del("session:" + sessionId);
					}
					logger.info("Session deleted from Redis");
				}
	}

	public void deleteCookieSession(String sessionId,HttpServletRequest request,HttpServletResponse response) {
		// Read SESSION_ID cookie
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("SESSION_ID".equals(cookie.getName())) {
					sessionId = cookie.getValue();
					logger.info("Session ID from cookie" + sessionId);

					// 2️ Delete cookie from browser
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					logger.info("Cookie deleted from browser");
					break;
				}
			}
		}
	}
}
