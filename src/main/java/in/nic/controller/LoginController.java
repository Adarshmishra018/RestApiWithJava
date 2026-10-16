package in.nic.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import in.nic.model.UserModel;
import in.nic.service.SessionService;
import in.nic.service.UserService;
import util.PasswordUtil;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("/login")
public class LoginController {
	UserService userService = new UserService();
	UserModel usermodel = null;
	boolean updated = false;
	String sessionId = UUID.randomUUID().toString();
	SessionService sessionService = new SessionService();
	@Context HttpServletResponse httpResponse;
	@Context HttpServletRequest httpRequest;
	private static final Logger logger = LogManager.getLogger(LoginController.class);

	// FOR LOGIN USER
	@POST // handles HTTP POST requests,Client must send a POST request
	@Path("/user") // Appends '/user' to this class
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) // method expects data in form-urlencoded form
	@Produces(MediaType.APPLICATION_JSON) // will send back JSON in the response
	public String loginNewUser(@FormParam("password") String password, @FormParam("email") String email,
			@Context HttpServletResponse httpResponse) {
														
		logger.info("Login Controller called for email: {}", email);
		password = PasswordUtil.hashPassword(password);
		logger.debug("email: {}", email);
		JSONObject response = new JSONObject(); // final JSON you will send to the client
		
		try {
			usermodel = userService.getUser(email, password);
			if (usermodel == null) { // check null
				logger.warn("Authentication failed: Invalid email or password for {}", email);
				response.put("status", "danger");
				response.put("msg", "Invalid email or password");
				response.put("statusCode", 401);
				logger.debug("response : " + response);
				return response.toString();
			}
			logger.info("User authenticated successfully: email={}, userId={}", email, usermodel.getId());
		
			sessionService.createRedisSession(sessionId); // Store sessionID in Redis
			sessionService.createCookieSession(sessionId, httpResponse); // Set SESSION_ID in cookie
			logger.info("Session created and stored in Redis and cookie: sessionId={}", sessionId);
			
			JSONObject userJson = new JSONObject(); // for data
			userJson.put("id", usermodel.getId());
			userJson.put("fullName", usermodel.getFullName());
			userJson.put("email", usermodel.getEmail());
			userJson.put("mobile", usermodel.getMobile());

			response.put("status", "success");
			response.put("statusCode", 200);
			response.put("data", userJson);
			response.put("msg", "User Logged in Successfully!");

			logger.info("User logged in successfully: email={}", email);
			return response.toString();

		} catch (Exception e) {
			logger.error("Exception during user login for email: {}", email, e);
			response.put("status", "error");
			response.put("statusCode", 500);
			response.put("msg", "Internal server error");

			return response.toString();
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProfile(UserModel usermodel) {
		logger.info("Profile update called for userId: {}", usermodel != null ? usermodel.getId() : "null");
		boolean updated = userService.updateProfile(usermodel);
		JSONObject response = new JSONObject();

		if (!updated) {
			logger.warn("Profile update failed for userId: {}", usermodel != null ? usermodel.getId() : "null");
			response.put("status", "error");
			response.put("msg", "Profile Update failed");
			response.put("statusCode", 401);
			return Response.status(401).entity(response.toString()).build();
		}

		logger.info("Profile updated successfully for userId: {}", usermodel != null ? usermodel.getId() : "null");
		response.put("status", "success");
		response.put("msg", "Profile Updated successfully");
		response.put("statusCode", 200);

		return Response.ok(response.toString()).build();
	}

	// FORGOT PASSWORD	
	@POST
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String resetPassword(@FormParam("email") String email, @FormParam("password") String password) {
		logger.info("Password reset requested for email: {}", email);
		JSONObject response = new JSONObject(); // final JSON you will send to the client
		password = PasswordUtil.hashPassword(password);
		updated = userService.resetPassword(email, password);

		if (!updated) {
			logger.warn("Password reset failed for email: {}", email);
			response.put("status", "error"); // putting values in JSON Object
			response.put("statusCode", 500);
			response.put("msg", "Error in changing Password ");
			return response.toString();
		}

		logger.info("Password updated successfully for email: {}", email);
		response.put("status", "success"); // putting values in JSON Object
		response.put("statusCode", 200);
		response.put("msg", "Password updated Successfully!");
		return response.toString();
	}

	// FOR LOGOUT	 
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout() {
		logger.info("Logout called, clearing session: sessionId={}", sessionId);
		try {
			sessionService.deleteRedisSession(sessionId);
			sessionService.deleteCookieSession(sessionId, httpRequest, httpResponse);
			logger.info("Session successfully deleted from Redis and cookie: sessionId={}", sessionId);
		} catch (Exception e) {
			logger.error("Exception during logout: sessionId={}", sessionId, e);
		}
		
		// Build response
		JSONObject res = new JSONObject();
		res.put("status", "success");
		res.put("msg", "Logged out successfully");
		logger.info("User logged out successfully");

		return res.toString();
	}

}
