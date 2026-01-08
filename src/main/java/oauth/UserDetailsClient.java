package oauth;



import in.nic.config.ParichayConfig;
import in.nic.controller.profileController;
import in.nic.model.UserDetails;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDetailsClient {  //request for user details

	private static final Logger logger = LogManager.getLogger(profileController.class);
	
    public static UserDetailsClient getUserDetails(String accessToken) {

        Client client = ClientBuilder.newClient();

        Response response = client
                .target(ParichayConfig.USER_DETAILS_URL)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .get();

        return response.readEntity(UserDetailsClient.class);
    }
}

