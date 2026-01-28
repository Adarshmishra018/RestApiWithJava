package in.nic.model;

public class SamlUser {

    private String parichayId;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String mobileNo;
    private String userId;
    private String gender;
    private String dob;
	public String getParichayId() {
		return parichayId;
	}
	public void setParichayId(String parichayId) {
		this.parichayId = parichayId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	@Override
	public String toString() {
		return "SamlUser [parichayId=" + parichayId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", countryCode=" + countryCode + ", mobileNo=" + mobileNo + ", userId=" + userId + ", gender="
				+ gender + ", dob=" + dob + "]";
	}

   
}


//
//public class SamlUser implements Serializable {
//
//    private String nameId;
//    private String email;
//    private String uid;
//    private Map<String, String> attributes;
//
//    public String getNameId() {
//        return nameId;
//    }
//
//    public void setNameId(String nameId) {
//        this.nameId = nameId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public Map<String, String> getAttributes() {
//        return attributes;
//    }
//
//    public void setAttributes(Map<String, String> attributes) {
//        this.attributes = attributes;
//    }
//}
