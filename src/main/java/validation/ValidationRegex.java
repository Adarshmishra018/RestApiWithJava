package validation;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import in.nic.controller.profileController;

public class ValidationRegex {
	
	private static final Logger logger = LogManager.getLogger(profileController.class);
		
	 // REGEX PATTERNS
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z ]{2,50}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^[6-9]\\d{9}$");

    //private static final Pattern PASSWORD_PATTERN =
    //        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");
    
    
    public static String validateRegistration(String fullName,String email,String mobile, String password
    		//, String confirmPassword
    		) {

        if (isNullOrEmpty(fullName)) {
            return "Name is required";
        }
        if (!NAME_PATTERN.matcher(fullName).matches()) {
            return "Name must contain only letters and spaces (2–50 chars)";
        }

        if (isNullOrEmpty(email)) {
            return "Email is required";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email format";
        }

        if (isNullOrEmpty(mobile)) {
            return "Mobile number is required";
        }
        if (!MOBILE_PATTERN.matcher(mobile).matches()) {
            return "Mobile number must be a valid 10-digit Indian number";
        }

        if (isNullOrEmpty(password)) {
            return "Password is required";
        }
      /*  if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "Password must be at least 8 characters and include uppercase, lowercase, number & special character";
        }*/

      /*  if (isNullOrEmpty(confirmPassword)) {
            return "Confirm password is required";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }
*/
        return null; // VALID
    }

    // HELPER METHOD
    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
