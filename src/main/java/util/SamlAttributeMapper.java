package util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensaml.saml.saml2.core.*;

import in.nic.model.SamlUser;

// converts SAML Attribute to Java SamlUser object
public class SamlAttributeMapper {

    private static final Logger logger = LogManager.getLogger(SamlAttributeMapper.class);

    public static SamlUser mapToUser(Assertion assertion) {
        SamlUser user = new SamlUser();
        int mappedCount = 0;

        logger.debug("Starting mapping of SAML assertion attribute statements");

        for (AttributeStatement stmt : assertion.getAttributeStatements()) {
        	logger.info("stmt={}", stmt);
            for (Attribute attr : stmt.getAttributes()) {
                String attrName = attr.getName();
                String attrValue = null;

                if (!attr.getAttributeValues().isEmpty()) {
                    org.w3c.dom.Element dom = attr.getAttributeValues().get(0).getDOM();
                    if (dom != null) {
                        attrValue = dom.getTextContent();
                    }
                }

                if (attrValue == null) {
                    continue;
                }

                logger.info("Mapping SAML attribute: key={}", attrName);

                switch (attrName) {
                    case "parichayId":
                        user.setParichayId(attrValue);
                        mappedCount++;
                        break;
                    case "firstName":
                        user.setFirstName(attrValue);
                        mappedCount++;
                        break;
                    case "lastName":
                        user.setLastName(attrValue);
                        mappedCount++;
                        break;
                    case "countryCode":
                        user.setCountryCode(attrValue);
                        mappedCount++;
                        break;
                    case "mobileNo":
                        user.setMobileNo(attrValue);
                        mappedCount++;
                        break;
                    case "userId":
                        user.setUserId(attrValue);
                        mappedCount++;
                        break;
                    case "gender":
                        user.setGender(attrValue);
                        mappedCount++;
                        break;
                    case "dob":
                        user.setDob(attrValue);
                        mappedCount++;
                        break;
                    default:
                        logger.debug("Unhandled SAML attribute: {}", attrName);
                        break;
                }
            }
        }

        logger.info("Completed SAML attribute mapping. Total mapped attributes: {}", mappedCount);
        return user;
    }
}
