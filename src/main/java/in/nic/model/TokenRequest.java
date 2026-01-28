package in.nic.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenRequest {

    @JsonProperty("grant_type")			//maps "grant_type" in JSON to grant_type in Java
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("redirect_uri")
    private String redirectUri;

    @JsonProperty("code")
    private String code;

    @JsonProperty("code_verifier")
    private String codeVerifier;

    // -------- Getters --------

    public String getGrantType() {
        return grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getCode() {
        return code;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    // -------- Setters --------

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

	@Override
	public String toString() {
		return "TokenRequest [grantType=" + grantType + ", clientId=" + clientId + ", clientSecret=" + clientSecret
				+ ", redirectUri=" + redirectUri + ", code=" + code + ", codeVerifier=" + codeVerifier + "]";
	}
    
    
}

