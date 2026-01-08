package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import in.nic.controller.LoginController;

public class MongoDbUtil {

	private static final Logger logger = LogManager.getLogger(MongoDbUtil.class);
	private static MongoClient mongoClient=MongoClients.create("mongodb://localhost:27017");//Connects to the MDB server
	
	public static MongoCollection<Document>	getUserCollection(){  //returns collection of document type
		MongoDatabase database = mongoClient.getDatabase("testdb");//Connects to the database 
		 logger.info("Connected to Server & Database");
		return database.getCollection("user"); //returns the collection named user
		
	}
	public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("MongoDB connection closed");
        }
    }
	
}
