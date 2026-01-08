package in.nic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import in.nic.model.UserModel;
import in.nic.service.UserService;
//import session.AuthFilter;

@Path("/profile")
public class profileController {
	
	UserService userservice=new UserService() ;
	 UserModel usermodel = new UserModel();
	private static final Logger logger = LogManager.getLogger(profileController.class);
//	
//	@Context
//	HttpServletRequest request;
//	
//	@GET
//	@Path("/user")
//	@Produces(MediaType.APPLICATION_JSON)
//	public String profile() {
//
//	    String email = (String) request.getAttribute("userEmail");			//reads the email from the current HTTP request
//	    logger.debug("UserEmail from request object:",email);
//	    if (email == null) {												//if email not found in request object
//	        throw new WebApplicationException(
//	                "{\"status\":\"error\",\"msg\":\"Unauthorized\"}",			//throws exception
//	                Response.Status.UNAUTHORIZED);
//	        
//	    }
//
//	    JSONObject response = new JSONObject();
//	    response.put("status", "success");
//	    response.put("statusCode", 200);
//		/*
//		 * response.put("username", usermodel.getFullName()); response.put("id",
//		 * usermodel.getId()); response.put("email", usermodel.getEmail());
//		 * response.put("mobile", usermodel.getMobile());
//		 */
//	    logger.debug("response:", response);
//	    logger.info("User into Profile page");
//	    
//	    return response.toString();
//	}
//	
	
//	@POST
//	@Path("/update")
//	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public String updateProfile(
//	        @FormParam("userid") String userId,
//	        @FormParam("username") String username,
//	        @FormParam("email") String email,
//	        @FormParam("mobile") String mobile) {
//
//	   
//	    usermodel.setId(userId);
//	    usermodel.setFullName(username);
//	    usermodel.setEmail(email);
//	    usermodel.setMobile(mobile);
//
//	    boolean updated = userservice.updateProfile(usermodel);
//
//	    JSONObject response = new JSONObject();
//	    if (!updated) {
//	    	 response.put("status", "error");
//	    	 response.put("msg", "Profile Update failed");
//	    	 response.put("statusCode", 401);
//	 		response.put("data", new JSONObject());
//	 	    logger.debug("response:", response);
//	 	    logger.info("User into Profile page");
//	 	   return response.toString();
//	    }
//	    response.put("status", "success");
//	    response.put("msg", "Profile Updated successfully");
//   	 	response.put("statusCode", 200);
//		response.put("data", new JSONObject());
// 	    logger.debug("response:", response);
// 	    logger.info("User into Profile page");
// 	   return response.toString();
// 	    
//	}
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateProfile(UserModel usermodel) {

	    boolean updated = userservice.updateProfile(usermodel);

	    JSONObject response = new JSONObject();

	    if (!updated) {
	        response.put("status", "error");
	        response.put("msg", "Profile Update failed");
	        response.put("statusCode", 401);
	        response.put("data", new JSONObject());
	        return response.toString();
	    }

	    response.put("status", "success");
	    response.put("msg", "Profile Updated successfully");
	    response.put("statusCode", 200);
	    response.put("data", new JSONObject());

	    return response.toString();
	}

	
	
}
