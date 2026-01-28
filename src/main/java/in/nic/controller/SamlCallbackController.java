package in.nic.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.opensaml.core.config.InitializationService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Base64;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.nic.model.SamlUser;
import util.SamlAttributeMapper;


import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.saml.saml2.core.*;
import org.opensaml.core.xml.util.XMLObjectSupport;

//SAML login callback handler 

@WebServlet("/saml/home")
public class SamlCallbackController extends HttpServlet {		

	private static final Logger logger = LogManager.getLogger(SamlCallbackController.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

    	logger.debug("Inside saml/home");
    	
    	response.setContentType("text/html;charset=UTF-8");

    	try {
    		
            // OpenSAML initialize to parse or create SAML objects,
            InitializationService.initialize();	// This loads OpenSAML internals parsers, builders, security configs

            //Read Base64-encoded SAMLResponse parameter sent by the IdP
            String samlResponseB64 = request.getParameter("SAMLResponse");
            if (samlResponseB64 == null) {
                response.getWriter().println("Missing SAMLResponse");
                return;
            }
            
            logger.debug("samlResponseB64{}",samlResponseB64);
            
            byte[] decodedBytes =Base64.getDecoder().decode(samlResponseB64);//Base64 Decode and store in bytes
            String samlXml =new String(decodedBytes, StandardCharsets.UTF_8);//converts bytes to string
            
            logger.debug("SAMLResponse decoded\n");

            //Convert XML into OpenSAML Java Object to easily access Assertions,Attribute,NameID,Conditions,SessionIndex
            XMLObject xmlObject =
                   XMLObjectSupport.unmarshallFromReader(             //Reads XML text//Parses it into a DOM document//Identifies the root element //Uses registered builders//Creates a Java object tree
                          XMLObjectProviderRegistrySupport
                                   .getParserPool(),
                           new java.io.StringReader(samlXml));
            
            logger.debug("xmlObject: {}",xmlObject);

            Response samlResponse = (Response) xmlObject; //Cast to Response type

         // check if response is empty
            if (samlResponse.getAssertions().isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "No Assertion found in SAMLResponse");
                return;
            }
            //Get the First Assertion //  Assertion has proof of authentication,user attributes
            Assertion assertion = samlResponse.getAssertions().get(0);
            
            //Map Assertion values to SamlUser 
            SamlUser user = SamlAttributeMapper.mapToUser(assertion);
            logger.debug("user: {}",user);
         
            // Store SAMLUser details in session
            HttpSession session = request.getSession(true);
            session.setAttribute("USER", user);
          
            DateTime notOnOrAfterDt = assertion.getConditions().getNotOnOrAfter();//session validity time sent by Idp
            Instant notOnOrAfter = notOnOrAfterDt.toInstant();// convert in instant type
            session.setAttribute("notOnOrAfter",notOnOrAfter);		//store in session
            
            logger.debug("notOnOrAfter {}",session.getAttribute("notOnOrAfter"));
            
             //store NameID & SessionIndex for logout
            session.setAttribute("SAML_NAME_ID",user.getUserId());	// set name id as user id       
            logger.debug("user id {}",session.getAttribute("SAML_NAME_ID"));

            //set session index from assertion file
            session.setAttribute( "SAML_SESSION_INDEX", assertion.getAuthnStatements().get(0).getSessionIndex());
            logger.debug("SESSION_INDEX {}",session.getAttribute("SAML_SESSION_INDEX"));

            // Redirect to profile page
            RequestDispatcher rd = request.getRequestDispatcher("/samlProfile.jsp");
            rd.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "SAML processing failed");
        }
    }
}
