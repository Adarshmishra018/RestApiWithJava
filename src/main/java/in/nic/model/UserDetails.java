package in.nic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDetails {
	
	
	
	@JsonProperty("ParichayId")
    private String parichayId;

    @JsonProperty("UserId")
    private String userId;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("dob")
    private String dob;   // or LocalDate (see below)

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Mobile")
    private String mobile;

    @JsonProperty("MobileNo")
    private String mobileNo;

    @JsonProperty("EmailId")
    private String emailId;

    @JsonProperty("ProfilePic")
    private String profilePic;

    
    
    
	public String getParichayId() {
		return parichayId;
	}

	public void setParichayId(String parichayId) {
		this.parichayId = parichayId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@Override
	public String toString() {
		return "UserDetails [parichayId=" + parichayId + ", userId=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dob=" + dob + ", gender=" + gender + ", mobile=" + mobile
				+ ", mobileNo=" + mobileNo + ", emailId=" + emailId + ", profilePic=" + profilePic + "]";
	}

	
}
