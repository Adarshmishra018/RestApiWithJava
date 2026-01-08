package util;

//To Convert String into ObjectId and vise-versa

import org.bson.types.ObjectId;

//utility class for MongoDB’s ObjectId

public final class ObjectIdUtil {				//final class Prevents inheritance
	
   
    private ObjectIdUtil() {}					//Prevents object creation

    
     //Convert String into ObjectId
      
    public static ObjectId toObjectId(String id) {
        if (id == null || id.isBlank()) {			//Avoids NullPointerException
            return null;
        }

        if (!ObjectId.isValid(id)) {			//Ensures the string Matches MongoDB ObjectId format
            throw new IllegalArgumentException("Invalid ObjectId string: " + id);
        }

        return new ObjectId(id);
    }

    
     // Converts ObjectId into String
    
    public static String toString(ObjectId objectId) {
        if (objectId == null) {			//Avoids NullPointerException
            return null;
        }

        return objectId.toHexString();				//Convert to hex string
    }
}

