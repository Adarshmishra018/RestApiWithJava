package in.nic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenResponse {			//Response of RefreshToken API

	@JsonProperty("access_token") //maps "access_token" in JSON to accessToken in Java
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("scope")
    private String scope;
    
    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private String expiresIn;

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

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "RefreshTokenResponse [accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", scope="
				+ scope + ", idToken=" + idToken + ", createdAt=" + createdAt + ", tokenType=" + tokenType
				+ ", expiresIn=" + expiresIn + "]";
	}
}
