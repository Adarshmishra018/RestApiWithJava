package in.nic.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {			////Response of request Token API

    @JsonProperty("access_token")	
    private String accessToken;

    @JsonProperty("refresh_token")		//maps "refresh_token" in JSON to refresh_token in Java
    private String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("scope")
    private String scope;
    
    @JsonProperty("Status")
    private String Status;


    @JsonProperty("Description")
    private String Description;

    @JsonProperty("Code")
    private String Code;

    @JsonProperty("id_token")
    private String idToken;

    //getters & setters
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		this.Status = status;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		this.Code = code;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	@Override
	public String toString() {
		return "TokenResponse [accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", expiresIn="
				+ expiresIn + ", tokenType=" + tokenType + ", createdAt=" + createdAt + ", scope=" + scope + ", Status="
				+ Status + ", Description=" + Description + ", Code=" + Code + ", idToken=" + idToken + "]";
	}
    
}
