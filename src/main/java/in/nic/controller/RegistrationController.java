//package in.nic.controller;
//
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import javax.ws.rs.Consumes;
//
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.json.JSONObject;
//
//import in.nic.model.UserModel;
//import in.nic.service.UserService;
////import session.AuthFilter;
//import validation.ValidationRegex;
//
//@Path("/register")
//public class RegistrationController {
//
//	UserService userservice = new UserService();
//	boolean exists = false;
//	private static final Logger logger = LogManager.getLogger(RegistrationController.class);
//	
//	@POST								//handles HTTP POST requests,Client must send a POST request
//	@Path("/user")						//Appends '/user' to this class
//	@Consumes(MediaType.APPLICATION_JSON)				//method expects data in JSON 
//	@Produces(MediaType.APPLICATION_JSON)				// will send back JSON in the response
//	public String registerNewUser(UserModel userModel) {
//		String email = (userModel != null) ? userModel.getEmail() : "null";
//		logger.info("registerNewUser called for email: {}", email);
//		JSONObject response = new JSONObject();			//JSON Object 
//		JSONObject data = new JSONObject();				//inner JSON object (nested inside response)
//
//		try {                                           //validate details
//			String error = ValidationRegex.validateRegistration(userModel.getFullName(), userModel.getEmail(),
//					userModel.getMobile(), userModel.getPassword()
//			);
//
//			//check validation 
//			if (error != null) {
//				logger.warn("Validation failed for email {}: {}", email, error);
//				response.put("status", "error");
//				response.put("statusCode", 400);
//				response.put("msg", error);
//				response.put("data", new JSONObject());
//				return response.toString();
//			}
//
//			//Check if user already exists
//			exists = userservice.registerUser(userModel);
//			if (exists) { //if found
//				logger.warn("User already exists in DB with email: {}", email);
//				response.put("status", "error");
//				response.put("statusCode", 400);
//				response.put("msg", "User Already exists");
//				response.put("data", new JSONObject());
//				return response.toString();
//			}
//
//			logger.info("User registered successfully in database: email={}", email);
//			response.put("status", "success"); // putting values in JSON Object
//			response.put("statusCode", 200);
//			response.put("data", data);
//			response.put("msg", "User Register Successfully!");
//
//			return response.toString();
//		} catch (Exception e) {
//			logger.error("Exception during user registration for email: {}", email, e);
//			response.put("status", "error");
//			response.put("statusCode", 500);
//			response.put("msg", "Internal server error");
//			return response.toString();
//		}
//	}
//
//}
