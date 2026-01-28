package util;


import org.opensaml.saml.saml2.core.*;

import in.nic.model.SamlUser;


//converts SAML Attribute to Java SamlUser object
public class SamlAttributeMapper {

    public static SamlUser mapToUser(Assertion assertion) {

        SamlUser user = new SamlUser();	//create a blank user object

        for (AttributeStatement stmt : assertion.getAttributeStatements()) {//get all AttributeStatements.
            for (Attribute attr : stmt.getAttributes()) {	//Loop Through Each Attribute

                String attrName = attr.getName();		//Read Attribute Name
                String attrValue = null;

                if (!attr.getAttributeValues().isEmpty()) {			//Extract Attribute Value
                    attrValue = attr.getAttributeValues()
                                    .get(0)
                                    .getDOM()
                                    .getTextContent();
                }

                if (attrValue == null) //Skip Empty Values
                	continue;

                switch (attrName) {			//Map Attribute to SAMLUser Fields
                    case "parichayId":
                        user.setParichayId(attrValue);
                        break;
                    case "firstName":
                        user.setFirstName(attrValue);
                        break;
                    case "lastName":
                        user.setLastName(attrValue);
                        break;
                    case "countryCode":
                        user.setCountryCode(attrValue);
                        break;
                    case "mobileNo":
                        user.setMobileNo(attrValue);
                        break;
                    case "userId":
                        user.setUserId(attrValue);
                        break;
                    case "gender":
                        user.setGender(attrValue);
                        break;
                    case "dob":
                        user.setDob(attrValue);
                        break;
                }
            }
        }

        return user;		//return a fully populated user object.
    }
}
