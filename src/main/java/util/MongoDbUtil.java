package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDbUtil {

	private static final Logger logger = LogManager.getLogger(MongoDbUtil.class);
	private static MongoClient mongoClient;
	
	static {
		try {
			logger.info("Initializing MongoDB client connection to mongodb://localhost:27017");
			mongoClient = MongoClients.create("mongodb://localhost:27017");
			logger.info("Successfully initialized MongoDB client instance");
		} catch (Exception e) {
			logger.error("Failed to initialize MongoDB client", e);
		}
	}
	
	public static MongoCollection<Document> getUserCollection() {
		logger.debug("Accessing MongoDB database 'testdb' and collection 'user'");
		MongoDatabase database = mongoClient.getDatabase("testdb");
		return database.getCollection("user");
	}
	
	public static void close() {
		if (mongoClient != null) {
			logger.info("Closing MongoDB client connection pool");
			mongoClient.close();
			logger.info("MongoDB client connection pool closed successfully");
		}
	}
}
