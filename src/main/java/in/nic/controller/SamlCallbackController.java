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

    	logger.info("Received SAML callback at /saml/home");
    	
    	response.setContentType("text/html;charset=UTF-8");

    	try {
            // OpenSAML initialization
            InitializationService.initialize();

            // Read Base64-encoded SAMLResponse parameter sent by the IdP
            String samlResponseB64 = request.getParameter("SAMLResponse");
            if (samlResponseB64 == null || samlResponseB64.isBlank()) {
                logger.warn("SAML callback received without 'SAMLResponse' parameter from IdP");
                response.getWriter().println("Missing SAMLResponse");
                return;
            }
            
            logger.info("SAMLResponse parameter received (length: {} chars). Decoding payload...", samlResponseB64.length());
            
            byte[] decodedBytes = Base64.getDecoder().decode(samlResponseB64);
            String samlXml = new String(decodedBytes, StandardCharsets.UTF_8);
            logger.debug("Successfully decoded SAML XML payload (length: {} chars)", samlXml.length());

            // Convert XML into OpenSAML Java Object
            XMLObject xmlObject = XMLObjectSupport.unmarshallFromReader(
                    XMLObjectProviderRegistrySupport.getParserPool(),
                    new java.io.StringReader(samlXml));
            
            logger.debug("Successfully unmarshalled XML into OpenSAML XMLObject: {}", xmlObject.getElementQName());

            Response samlResponse = (Response) xmlObject;

            // Check if response contains assertions
            if (samlResponse.getAssertions().isEmpty()) {
                logger.warn("SAMLResponse validation failed: No assertions found in IdP response");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "No Assertion found in SAMLResponse");
                return;
            }

            // Get the First Assertion
            Assertion assertion = samlResponse.getAssertions().get(0);
            logger.info("Extracting attributes from SAML Assertion ID: {}", assertion.getID());
            
            // Map Assertion values to SamlUser 
            SamlUser user = SamlAttributeMapper.mapToUser(assertion);
            logger.info("Successfully mapped SAML assertion to user: userId={}, name={} {}", 
                    user.getUserId(), user.getFirstName(), user.getLastName());
         
            // Store SAMLUser details in session
            HttpSession session = request.getSession(true);
            session.setAttribute("USER", user);
            logger.info("User session established for SAML user: sessionId={}", session.getId());
          
            if (assertion.getConditions() != null && assertion.getConditions().getNotOnOrAfter() != null) {
                DateTime notOnOrAfterDt = assertion.getConditions().getNotOnOrAfter();
                Instant notOnOrAfter = notOnOrAfterDt.toInstant();
                session.setAttribute("notOnOrAfter", notOnOrAfter);
                logger.debug("SAML assertion validity expires at: {}", notOnOrAfter);
            }
            
            // Store NameID & SessionIndex for logout
            session.setAttribute("SAML_NAME_ID", user.getUserId());

            if (!assertion.getAuthnStatements().isEmpty() && assertion.getAuthnStatements().get(0).getSessionIndex() != null) {
                String sessionIndex = assertion.getAuthnStatements().get(0).getSessionIndex();
                session.setAttribute("SAML_SESSION_INDEX", sessionIndex);
                logger.debug("Stored SAML sessionIndex for SLO: {}", sessionIndex);
            }

            // Forward to profile page
            logger.info("Forwarding authenticated SAML user to /samlProfile.jsp");
            RequestDispatcher rd = request.getRequestDispatcher("/samlProfile.jsp");
            rd.forward(request, response);
            
        } catch (Exception e) {
            logger.error("Exception occurred while processing SAMLResponse callback", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "SAML processing failed");
        }
    }
}
