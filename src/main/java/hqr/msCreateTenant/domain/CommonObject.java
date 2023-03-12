package hqr.msCreateTenant.domain;

public class CommonObject {
	private String token;
	private String challengeId;
	private String challengeString;
	private String azureRegion;
	private String newTenantId;
	private String dirName;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}
	public String getChallengeString() {
		return challengeString;
	}
	public void setChallengeString(String challengeString) {
		this.challengeString = challengeString;
	}
	public String getAzureRegion() {
		return azureRegion;
	}
	public void setAzureRegion(String azureRegion) {
		this.azureRegion = azureRegion;
	}
	public String getNewTenantId() {
		return newTenantId;
	}
	public void setNewTenantId(String newTenantId) {
		this.newTenantId = newTenantId;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
}
