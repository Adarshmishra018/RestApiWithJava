package in.nic.model;

import util.PasswordUtil;

public class UserModel {

	private String id;
	private String fullName;
	private String email;
	private String userId;
	private String password;
	private String mobile;

	//String hashedPassword =PasswordUtil.hashPassword(password);
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public UserModel() {
	}

	
	
	@Override
	public String toString() {
		return "UserModel [id=" + id + ", fullName=" + fullName + ", email=" + email + ", userId=" + userId
				+ ", password=" + password + ", mobile=" + mobile + "]";
	}

}
