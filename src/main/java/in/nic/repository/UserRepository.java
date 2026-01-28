package in.nic.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;


import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import in.nic.controller.LoginController;
import in.nic.model.UserModel;
import util.MongoDbUtil;
import util.ObjectIdUtil;
import util.PasswordUtil;

public class UserRepository {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	UserModel userModel = new UserModel();
	MongoCollection<Document> users = MongoDbUtil.getUserCollection();

	//INSERT NEW USER
	public void save(UserModel userModel) {
		String password = PasswordUtil.hashPassword(userModel.getPassword());
		Document user = new Document("fullName", userModel.getFullName()).append("email", userModel.getEmail())
				.append("password", password).append("mobile", userModel.getMobile()); // putting your user data inside																			// // user.
		users.insertOne(user);
		logger.info("User details inserted in DB");
	}

	//USED FOR LOGIN
	public UserModel findByEmail(String email, String password) {
	    try {
	       
	        Document doc = users.find(				//find user all details & store in doc
	                Filters.and(
	                        Filters.eq("email", email),
	                        Filters.eq("password", password)
	                )
	        ).first();

	        if (doc == null) {
	            logger.warn("No user found for email: {}", email);
	            return null;
	        }

	        //set user Model as user is in doc
	        UserModel usermodel = new UserModel();
	        usermodel.setId(ObjectIdUtil.toString(doc.getObjectId("_id")));		
	        usermodel.setFullName(doc.getString("fullName"));//convert doc in string and set in userModel
	        usermodel.setEmail(doc.getString("email"));
	        usermodel.setMobile(doc.getString("mobile"));
	        usermodel.setPassword(doc.getString("password"));

	        logger.info("User found in DB for email: {}", email);
	        return usermodel;

	    } catch (IllegalArgumentException e) {
	        logger.error("Invalid argument while fetching user", e);
	        return null;

	    } catch (MongoException e) {
	        logger.error("MongoDB error while fetching user", e);
	        return null;

	    } catch (Exception e) {
	        logger.error("Unexpected error while fetching user", e);
	        return null;
	    }
	}


	//CHECKS IF USER ALREADY EXISTS
	public boolean existsByEmail(String email) {
		Document user = users.find(Filters.eq("email", email)).first();// find in DB and store in user Document
		if (user != null)
			return true; // User details found
		return false; // User details not found
	}

	//UPDATE USER DETAILS
	public boolean updateUser(UserModel user) {
		
		UpdateResult result = users.updateOne(Filters.eq("_id", ObjectIdUtil.toObjectId(user.getId())),
				Updates.combine(Updates.set("fullName", user.getFullName()), Updates.set("email", user.getEmail()),
						Updates.set("mobile", user.getMobile())));
		return result.getModifiedCount() > 0;
	}

	//FORGOT PASSWORD
	public boolean updatePassword(String email, String password) {
		UpdateResult result = users.updateOne(Filters.eq("email", email), Updates.set("password", password));
		return result.getModifiedCount() > 0; // how many documents were actually changed
	}

}
