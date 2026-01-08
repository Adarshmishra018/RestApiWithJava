package in.nic.controller;

import java.net.http.HttpResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import in.nic.model.UserModel;
import in.nic.service.UserService;
//import session.AuthFilter;
import validation.ValidationRegex;

@Path("/register")
public class RegistrationController {

	UserService userservice=new UserService() ;
	boolean exists=false;
	private static final Logger logger = LogManager.getLogger(RegistrationController.class);
	
	@GET
	@Path("/tst")
	public String test() {
		return "Jersey 2 is working";
	}
	

//	public String registerNewUser(
//	@FormParam("password") String password, @FormParam("mobile") String mobile,
//	@FormParam("email") String email, @FormParam("name") String name) {

	
	
	@POST								//handles HTTP POST requests,Client must send a POST request
	@Path("/user")						//Appends '/user' to this class
	@Consumes(MediaType.APPLICATION_JSON)				//method expects data in JSON 
	@Produces(MediaType.APPLICATION_JSON)				// will send back JSON in the response
	public String registerNewUser(UserModel userModel) {
		logger.info("registerNewuser called");//reads the form data from userModel
		logger.debug("userModel : " + userModel);
		JSONObject response = new JSONObject();			//JSON Object 
		JSONObject data = new JSONObject();				//inner JSON object (nested inside response)

		try {                                           //validate details
			String error = ValidationRegex.validateRegistration(userModel.getFullName(), userModel.getEmail(),
					userModel.getMobile(), userModel.getPassword()
			);

			if (error != null) {//check validation 
				logger.error("Error in filling details");
				response.put("status", "error");
				response.put("statusCode", 400);
				response.put("msg", "Enter valid Details");
				response.put("data", new JSONObject());
				return response.toString();
			}

			exists = userservice.registerUser(userModel);//Check if user already exists0
			if (exists) { // found
				logger.error("User Already exists");
				response.put("status", "error");
				response.put("statusCode", 400);
				response.put("msg", "User Already exists");
				response.put("data", new JSONObject());
				return response.toString();
			}

			logger.info("User validation sucessful");
			// saving 1 data inside DB.
			logger.info("User Registered successfully");
			logger.debug("response : " + response);

			logger.info("Registration called");
			response.put("status", "success"); // putting values in JSON Object
			response.put("statusCode", 200);
			response.put("data", data);
			response.put("msg", "User Register Successfully!");

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("statusCode", 500);
			response.put("msg", "Internal server error");
			logger.error("Error while registering:" + response);
			return response.toString();
		}
	}

}
