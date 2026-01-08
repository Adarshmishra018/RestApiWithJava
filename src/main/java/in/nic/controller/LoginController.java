package in.nic.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import in.nic.model.UserModel;
import in.nic.service.UserService;
import redis.clients.jedis.Jedis;
import util.PasswordUtil;
import util.RedisUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("/login")
public class LoginController {
	UserService userService = new UserService();
	UserModel usermodel = null;
	boolean updated = false;
	
	@Context 							// injects the current HTTP request object into your Jersey resource
	HttpServletRequest request; // gives you access to the incoming HTTP request

	@Context 								// injects the current HTTP respose object into your Jersey resource
	HttpServletResponse response; // gives you control over the outgoing HTTP response

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	// FOR LOGIN USER
	@POST 								// handles HTTP POST requests,Client must send a POST request
	@Path("/user") 							// Appends '/user' to this class
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) // method expects data in form-urlencoded form
	@Produces(MediaType.APPLICATION_JSON) // will send back JSON in the response
	public String loginNewUser(@FormParam("password") String password, @FormParam("email") String email,
			@Context HttpServletResponse httpResponse) {// reads the form data from the request body
		logger.info("Login Controller called");
		password = PasswordUtil.hashPassword(password);
		logger.debug(" email" + email);
		logger.debug(" Password" + password);
		JSONObject response = new JSONObject(); // final JSON you will send to the client
		JSONObject data = new JSONObject(); //

		try {
			usermodel = userService.getUser(email, password);
			if (usermodel == null) { // User NOT found
				logger.debug("usermodel is " + usermodel);
				response.put("status", "danger");
				response.put("msg", "Invalid email or password");
				response.put("statusCode", 401);
//				response.put("username", usermodel.getFullName());
//				response.put("id", usermodel.getId());
//				response.put("email", usermodel.getEmail());
//				response.put("mobile", usermodel.getMobile());

				logger.debug("response : " + response);
				return response.toString();
			}
			logger.debug("usermodel is " + usermodel);
			// Generate session ID
			String sessionId = UUID.randomUUID().toString();
			logger.info("session ID created" + sessionId);

			String sessionKey = "session:" + sessionId;


			// Store session in Redis
			try (Jedis jedis = RedisUtil.getConnection()) { // returns a Jedis connection
				jedis.setex( // SET a key with a value and EXPIRE it automatically after given time.
						"session:" + sessionId, 1800, // 30 minutes
						sessionId);

				logger.info("Session ID stored in Redis" + sessionId);
			}

			// Set SESSION_ID in cookie //creates a secure session cookie, sends it to the
			// browser, and links the browser session with the Redis session
			Cookie cookie = new Cookie("SESSION_ID", sessionId);
			cookie.setHttpOnly(true); // protection from XSS
			cookie.setPath("/"); // Cookie is sent for all URL
			cookie.setMaxAge(1800); // cookie expires after 30 mins
			httpResponse.addCookie(cookie); // Send cookie to browser
			logger.info("Session ID stored in cookie" + sessionId);

			// Build success response
			// data.put("email", email);
			JSONObject userJson = new JSONObject();
			userJson.put("id", usermodel.getId());
			userJson.put("fullName", usermodel.getFullName());
			userJson.put("email", usermodel.getEmail());
			userJson.put("mobile", usermodel.getMobile());

			response.put("status", "success");
			response.put("statusCode", 200);
			response.put("data", userJson);
			response.put("msg", "User Logged in Successfully!");
			response.put("email:", email);
			response.put("password", password);

			logger.debug("response : " + response);
			logger.info("User logged in sucessfully");

			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();

			response.put("status", "error");
			response.put("statusCode", 500);
			response.put("msg", "Internal server error");

			logger.error("Error while logging:" + response);
			return response.toString();
		}

	}

//FORGOT PASSWORD	
	@POST
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String resetPassword(@FormParam("email") String email, @FormParam("password") String password) {

		JSONObject response = new JSONObject(); // final JSON you will send to the client
		JSONObject data = new JSONObject(); //

		password = PasswordUtil.hashPassword(password);
		updated = userService.resetPassword(email, password);

		if (!updated) {
			logger.info("Error in changing Password ");
			response.put("status", "error"); // putting values in JSON Object
			response.put("statusCode", 500);
			response.put("data", data);
			response.put("msg", "Error in changing Password ");
			return response.toString();
		}

		logger.info("Password changed successfully");
		response.put("status", "success"); // putting values in JSON Object
		response.put("statusCode", 200);
		response.put("data", data);
		response.put("msg", "Password updated Successfully!");
		return response.toString();
	}

//FOR LOGOUT	 
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout() {

		String sessionId = null;
		logger.info("Logout called");

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

		// 3️.Delete session from Redis
		if (sessionId != null) {
			try (Jedis jedis = RedisUtil.getConnection()) {
				jedis.del("session:" + sessionId);
			}
			logger.info("Session deleted from Redis");
		}

		// 4️ Build response
		JSONObject res = new JSONObject();
		res.put("status", "success");
		res.put("msg", "Logged out successfully");
		logger.debug("Response:" + res);

		return res.toString();
	}
	
	
	
	

}
