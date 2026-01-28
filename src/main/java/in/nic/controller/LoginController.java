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
	String sessionId=UUID.randomUUID().toString();;
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
														
		logger.info("Login Controller called");
		password = PasswordUtil.hashPassword(password);
		logger.debug(" email" + email);
		logger.debug(" Password" + password);
		JSONObject response = new JSONObject(); // final JSON you will send to the client
		

		try {
			usermodel = userService.getUser(email, password);
			if (usermodel == null) { // User NOT found
				logger.debug("usermodel is " + usermodel);
				response.put("status", "danger");
				response.put("msg", "Invalid email or password");
				response.put("statusCode", 401);
				logger.debug("response : " + response);
				return response.toString();
			}
			logger.debug("usermodel is " + usermodel);
			
			 
		
			sessionService.createRedisSession(sessionId);// Store sessionID in Redis
			sessionService.createCookieSession(sessionId,httpResponse);// Set SESSION_ID in cookie
				
			logger.debug("sessionId stored in both : " + sessionId);
			logger.debug("response : " + response);
			
			JSONObject userJson = new JSONObject();
			userJson.put("id", usermodel.getId());
			userJson.put("fullName", usermodel.getFullName());
			userJson.put("email", usermodel.getEmail());
			userJson.put("mobile", usermodel.getMobile());

			response.put("status", "success");
			response.put("statusCode", 200);
			response.put("data", userJson);
			response.put("msg", "User Logged in Successfully!");

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

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProfile(UserModel usermodel) {

		boolean updated = userService.updateProfile(usermodel);
		logger.debug("Values Updated");
		JSONObject response = new JSONObject();

		if (!updated) {
			response.put("status", "error");
			response.put("msg", "Profile Update failed");
			response.put("statusCode", 401);
			return Response.status(401).entity(response.toString()).build();
		}

		response.put("status", "success");
		response.put("msg", "Profile Updated successfully");
		response.put("statusCode", 200);

		return Response.ok(response.toString()).build();
	}

//FORGOT PASSWORD	
	@POST
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String resetPassword(@FormParam("email") String email, @FormParam("password") String password) {

		JSONObject response = new JSONObject(); // final JSON you will send to the client
		password = PasswordUtil.hashPassword(password);
		updated = userService.resetPassword(email, password);

		if (!updated) {
			logger.info("Error in changing Details ");
			response.put("status", "error"); // putting values in JSON Object
			response.put("statusCode", 500);
			response.put("msg", "Error in changing Password ");
			return response.toString();
		}

		logger.info("Details changed successfully");
		response.put("status", "success"); // putting values in JSON Object
		response.put("statusCode", 200);
		response.put("msg", "Password updated Successfully!");
		return response.toString();
	}

//FOR LOGOUT	 
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout() {

		logger.info("Logout called");
		try {

			sessionService.deleteRedisSession(sessionId);
			sessionService.deleteCookieSession(sessionId,httpRequest,httpResponse);
			logger.debug(" sessionId deleted: " + sessionId);
		} catch (Exception e) {
			logger.debug("Exception {}",e);
		}
		
		// Build response
		JSONObject res = new JSONObject();
		res.put("status", "success");
		res.put("msg", "Logged out successfully");
		logger.debug("Response:" + res);

		return res.toString();
	}

}
