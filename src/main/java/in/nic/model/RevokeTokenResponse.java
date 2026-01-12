package in.nic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RevokeTokenResponse {				//Response of Revoke Token API
	@JsonProperty("Status")							//maps "Status" in JSON to status in Java
    private String status;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("Code")
    private String code;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "RevokeTokenResponse [status=" + status + ", description=" + description + ", tokenType=" + tokenType
				+ ", code=" + code + "]";
	}
}
