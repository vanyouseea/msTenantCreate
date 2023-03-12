package hqr.msCreateTenant.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class GetToken {
	
	private RestTemplate restTemplate = new RestTemplate();
	private String username = "";
	private String password = "";
	private String tenantId = "common";
	
	public String getMsToken() {
		String token = "";
		try {
			String endPoint = "https://login.microsoftonline.com/"+tenantId+"/oauth2/token";
			String json = "grant_type=password&resource=74658136-14ec-4630-ad9b-26e160ff0fc6&client_id=1950a258-227b-4e31-a9cf-717495945fc2&username="+username+"&password="+password;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
			ResponseEntity<String> response= restTemplate.postForEntity(endPoint, requestEntity, String.class);
			if(response.getStatusCodeValue()==200) {
				JSONObject jb = JSON.parseObject(response.getBody());
				token = jb.getString("access_token");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
