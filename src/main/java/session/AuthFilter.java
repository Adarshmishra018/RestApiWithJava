package session;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.json.JSONObject;
//
//import redis.clients.jedis.Jedis;
//import util.RedisUtil;
//
//@WebFilter("/*")   // Applies this filter to profile request
//public class AuthFilter implements Filter  {
//
//private static final Logger logger = LogManager.getLogger(AuthFilter.class);
//
//	
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		logger.info("AuthFilter initialized");
//	}
//		
//	//FilterChain is object provided by the Servlet container that represents the sequence (chain) of filters + target servlet.
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {//(security checkpoint)method runs for every HTTP request.//Runs for every request matched by the filter.
//
//        HttpServletRequest request = (HttpServletRequest) req;//type cast ServletRequest to HTTP-specific methods// contains all details of the incoming HTTP request
//        HttpServletResponse response = (HttpServletResponse) res;
//        //Disable cache
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
//		 response.setHeader("Pragma", "no-cache"); // HTTP 1.0
//		 response.setDateHeader("Expires", 0);
//
//        
//        
//        // Exclude login and register routes
//        String path = request.getRequestURI();//returns the path of the requested resource//
//        logger.info("Incoming request path: {}", path);
//
//        //These pages must be accessible without login
//        if (path.startsWith("/v1/api/login/user")
//                || path.startsWith("/v1/api/register/user")
//                ||path.startsWith("/v1/api/password/reset")
//                ||path.startsWith("v1/api/loginOAuth")
//                ||path.startsWith("/v1/api/profile/update")
//                || path.endsWith(".html")||path.equals("/v1/")
//                || path.endsWith(".js")) {
//            chain.doFilter(request, response); 		//passes the current request and response to the next filter or the target servlet so that processing can continue.
//            logger.info("login and register routes found");
//            return;
//        }
//       
//        
//        
//        // Step 1: Extract sessionId from cookie
//        String sessionId = null;
//        //also set SESSION ID in cookie
//        if (request.getCookies() != null) {				//Read SESSION_ID cookie
//            for (Cookie c : request.getCookies()) {			//Iterates over every cookie 
//                if ("SESSION_ID".equals(c.getName())) { 	//checks whether the current cookie’s name is SESSION_ID.
//                    sessionId = c.getValue();				//Extract the session ID
//                    break;
//                }
//            }
//            logger.info("Extracted sessionId from cookie:"+sessionId);
//        }
//
//        // Step 2: If no cookie → user not logged in
//        if (sessionId == null) {
//        	 logger.info("No session cookie found");
//        	 sendUnauthorized(response, "Session expired or invalid");
//            return;
//        }
//
//        // Step 3: Validate session in Redis
//        String sessionIdRedis = null;		
//        try (Jedis jedis = RedisUtil.getConnection()) {		//Opens a Redis connection  //try-with-resources → Automatically closes connection
//        	sessionIdRedis= jedis.get("session:" + sessionId);		// fetches the logged-in user’s data from Redis using the session ID as the key.
//       //check with sessionId is same
//        	logger.info("Extracted session from Redis"+sessionIdRedis);
//        }
//        
//
//        // Step 4: Redis returned null → invalid session
//        if (sessionIdRedis == null || !(sessionIdRedis.equals(sessionId))) {
//        	logger.info("Invalid session in Redis");
//        	 sendUnauthorized(response, "Session expired or invalid");
//            return;
//        }
//
//        // Step 5: Attach user info to request for servlets
//        request.setAttribute("userEmail", sessionIdRedis);		//stores the logged-in user’s information in the current HTTP request
//        logger.info("Attached user info to request ");
//        // Step 6: Allow request to continue
//        //All above code runs pre-processing
//        chain.doFilter(req, res);		//passes the request and response to the next filter or to the target servlet
//      //All below code will run post-processing
//    }
//	
//	private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
//
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		response.setContentType("application/json");
//
//		JSONObject json = new JSONObject();
//		json.put("status", "error");
//		json.put("statusCode", 401);
//		json.put("msg", message);
//
//		response.getWriter().print(json.toString());
//	}
//} 
