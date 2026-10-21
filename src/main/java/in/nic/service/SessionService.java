package in.nic.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import util.RedisUtil;

public class SessionService {

	private static final Logger logger = LogManager.getLogger(SessionService.class);

	public void createRedisSession(String sessionId) {
		String sessionKey = "session:" + sessionId;
		logger.info("Creating session in Redis: key={}, TTL=1800s", sessionKey);
		try (Jedis jedis = RedisUtil.getConnection()) {
			jedis.setex(sessionKey, 1800, sessionId);
			logger.info("Successfully persisted session in Redis: {}", sessionKey);
		} catch (Exception e) {
			logger.error("Failed to store session in Redis: {}", sessionKey, e);
		}
	}

	public void createCookieSession(String sessionId, HttpServletResponse response) {
		logger.info("Attaching SESSION_ID cookie to response: HttpOnly=true, Path=/");
		Cookie cookie = new Cookie("SESSION_ID", sessionId);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(1800);
		response.addCookie(cookie);
		logger.info("Successfully added SESSION_ID cookie to HTTP response");	
	}

	public void deleteRedisSession(String sessionId) {
		if (sessionId != null && !sessionId.isBlank()) {
			String sessionKey = "session:" + sessionId;
			logger.info("Deleting session from Redis: key={}", sessionKey);
			try (Jedis jedis = RedisUtil.getConnection()) {
				jedis.del(sessionKey);
				logger.info("Successfully deleted session from Redis: {}", sessionKey);
			} catch (Exception e) {
				logger.error("Failed to delete session from Redis: {}", sessionKey, e);
			}
		}
	}

	public void deleteCookieSession(String sessionId, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Clearing SESSION_ID cookie from client");
		if (request != null && request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("SESSION_ID".equals(cookie.getName())) {
					String cookieSessionId = cookie.getValue();
					logger.info("Found SESSION_ID cookie (sessionId={}). Deleting from Redis and expiring cookie.", cookieSessionId);

					// Also delete from Redis
					deleteRedisSession(cookieSessionId);

					// Delete cookie from browser
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					logger.info("Successfully expired SESSION_ID cookie in HTTP response");
					break;
				}
			}
		}
	}
}
