package in.nic.controller;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Base64Util;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.core.xml.util.XMLObjectSupport;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Response;

import org.opensaml.saml.saml2.core.LogoutResponse;
import org.opensaml.saml.saml2.core.StatusCode;


import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;


import net.shibboleth.utilities.java.support.xml.XMLParserException;

@WebServlet("/saml/logout")
public class SamlLogoutServlet extends HttpServlet {
	
		private static final Logger logger = LogManager.getLogger(SamlLogoutServlet.class);
	    private static final long serialVersionUID = 1L;
	   
	    
	    @Override
	    protected void doPost(HttpServletRequest request,
	                          HttpServletResponse response)
	            throws ServletException, IOException {
	    	logger.info("Received SAML Single Logout (SLO) callback at /saml/logout");
	    	try {
	    		InitializationService.initialize();
	    		 
	    		String samlResponseB64 = request.getParameter("SAMLResponse");
	            if (samlResponseB64 == null || samlResponseB64.isBlank()) {
	                logger.warn("SAML logout callback received without 'SAMLResponse' parameter");
	                response.getWriter().println("Missing SAMLResponse");
	                return;
	            }
	            
	            logger.debug("Received Base64 SAMLResponse length: {} chars", samlResponseB64.length());
	            
	            byte[] decodedBytes = Base64.getDecoder().decode(samlResponseB64);
	            String samlXml = new String(decodedBytes, StandardCharsets.UTF_8);
	            logger.debug("Decoded SAML Logout XML payload length: {} chars", samlXml.length());
	            
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            factory.setNamespaceAware(true);
	            // Protect against XXE
	            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.parse(new ByteArrayInputStream(samlXml.getBytes(StandardCharsets.UTF_8)));

	            Element root = doc.getDocumentElement();
	            logger.info("Parsed SAML Logout document: rootElement={}, ID={}, Version={}", 
	                    root.getNodeName(), root.getAttribute("ID"), root.getAttribute("Version"));

	            // Issuer
	            NodeList issuerList = doc.getElementsByTagNameNS(
	                    "urn:oasis:names:tc:SAML:2.0:assertion", "Issuer");
	            if (issuerList.getLength() > 0) {
	                logger.info("SAML Logout Issuer: {}", issuerList.item(0).getTextContent());
	            }

	            // Status Code
	            NodeList statusCodeList = doc.getElementsByTagNameNS(
	                    "urn:oasis:names:tc:SAML:2.0:protocol", "StatusCode");
	            if (statusCodeList.getLength() > 0) {
	                Element statusCode = (Element) statusCodeList.item(0);
	                logger.info("SAML Logout response status: {}", statusCode.getAttribute("Value"));
	            }

	            HttpSession session = request.getSession(false);
	            if (session != null) {
	                logger.info("Invalidating user session: sessionId={}", session.getId());
	                session.invalidate();
	                logger.info("User session successfully invalidated upon SAML logout");
	            } else {
	                logger.info("No active HTTP session found to invalidate during SAML logout");
	            }

	            logger.info("Redirecting user to post-logout landing page");
	            response.sendRedirect("http://jpmeripehchaan.staging.nic.in/index.html");

	        } catch (Exception e) {
	            logger.error("Exception occurred while processing SAML logout response", e);
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAML logout processing failed");
	        }
	    }

}
