package in.nic.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;


import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import in.nic.model.UserModel;
import util.MongoDbUtil;
import util.ObjectIdUtil;
import util.PasswordUtil;

public class UserRepository {

	private static final Logger logger = LogManager.getLogger(UserRepository.class);

	private final MongoCollection<Document> users = MongoDbUtil.getUserCollection();

	// INSERT NEW USER
	public void save(UserModel userModel) {
		logger.info("Inserting new user document into MongoDB: email={}, fullName={}", 
				userModel.getEmail(), userModel.getFullName());
		String password = PasswordUtil.hashPassword(userModel.getPassword());
		Document user = new Document("fullName", userModel.getFullName())
				.append("email", userModel.getEmail())
				.append("password", password)
				.append("mobile", userModel.getMobile());
		users.insertOne(user);
		logger.info("Successfully inserted user document into MongoDB collection");
	}

	// USED FOR LOGIN
	public UserModel findByEmail(String email, String password) {
	    logger.debug("Executing findByEmail query on MongoDB for email: {}", email);
	    try {
	        Document doc = users.find(
	                Filters.and(
	                        Filters.eq("email", email),
	                        Filters.eq("password", password)
	                )
	        ).first();

	        if (doc == null) {
	            logger.warn("No matching user document found in MongoDB for email: {}", email);
	            return null;
	        }

	        UserModel usermodel = new UserModel();
	        usermodel.setId(ObjectIdUtil.toString(doc.getObjectId("_id")));		
	        usermodel.setFullName(doc.getString("fullName"));
	        usermodel.setEmail(doc.getString("email"));
	        usermodel.setMobile(doc.getString("mobile"));
	        usermodel.setPassword(doc.getString("password"));

	        logger.info("Found user in MongoDB: id={}, email={}", usermodel.getId(), email);
	        return usermodel;

	    } catch (IllegalArgumentException e) {
	        logger.error("Invalid argument while querying user in MongoDB: email={}", email, e);
	        return null;

	    } catch (MongoException e) {
	        logger.error("MongoDB exception while querying user: email={}", email, e);
	        return null;

	    } catch (Exception e) {
	        logger.error("Unexpected error while querying user in MongoDB: email={}", email, e);
	        return null;
	    }
	}

	// CHECKS IF USER ALREADY EXISTS
	public boolean existsByEmail(String email) {
		logger.debug("Checking MongoDB for existence of user with email: {}", email);
		Document user = users.find(Filters.eq("email", email)).first();
		boolean found = (user != null);
		logger.debug("User existence check for email '{}': {}", email, found ? "EXISTS" : "NOT_FOUND");
		return found;
	}

	// UPDATE USER DETAILS
	public boolean updateUser(UserModel user) {
		logger.info("Updating user document in MongoDB: userId={}, email={}", user.getId(), user.getEmail());
		try {
			UpdateResult result = users.updateOne(
					Filters.eq("_id", ObjectIdUtil.toObjectId(user.getId())),
					Updates.combine(
							Updates.set("fullName", user.getFullName()),
							Updates.set("email", user.getEmail()),
							Updates.set("mobile", user.getMobile())
					)
			);
			boolean success = result.getModifiedCount() > 0;
			logger.info("MongoDB updateUser result: matchedCount={}, modifiedCount={}", 
					result.getMatchedCount(), result.getModifiedCount());
			return success;
		} catch (Exception e) {
			logger.error("MongoDB exception while updating user: userId={}", user.getId(), e);
			return false;
		}
	}

	// FORGOT PASSWORD
	public boolean updatePassword(String email, String password) {
		logger.info("Updating password for email in MongoDB: email={}", email);
		try {
			UpdateResult result = users.updateOne(
					Filters.eq("email", email), 
					Updates.set("password", password)
			);
			boolean success = result.getModifiedCount() > 0;
			logger.info("MongoDB updatePassword result: matchedCount={}, modifiedCount={}", 
					result.getMatchedCount(), result.getModifiedCount());
			return success;
		} catch (Exception e) {
			logger.error("MongoDB exception while updating password for email: {}", email, e);
			return false;
		}
	}

}
