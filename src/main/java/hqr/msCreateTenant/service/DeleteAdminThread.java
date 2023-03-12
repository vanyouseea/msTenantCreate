package hqr.msCreateTenant.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class DeleteAdminThread implements Runnable {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private GetToken getToken;
	private String token = "";
	private String username = "";
	private String newUsername;
	private String password;

	public GetToken getGetToken() {
		return getToken;
	}
	public void setGetToken(GetToken getToken) {
		this.getToken = getToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNewUsername() {
		return newUsername;
	}
	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void run() {
		//0.sleep 60s
		try {
			System.out.println("Wait 60s to delete old admin");
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//1.get token
		getToken.setUsername(newUsername);
		getToken.setPassword(password);
		getToken.setTenantId("common");
		token = getToken.getMsToken();
		
		//2.get old admin uid
		String oldAdminUid = getOldAdminUid();
		if("".equals(oldAdminUid)) {
			System.out.println("fail to find the old admin, skip delete action");
			return ;
		}
		
		//3.delete old admin
		deleteUser(oldAdminUid);
		
		//4.permanent delete old admin
		permanentDeleteUser(oldAdminUid);
	}
	
	private String getOldAdminUid() {
		String adminUid = "";
		username = username.replaceAll("@", "_");
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Users?searchText="+username+"&top=25&orderByThumbnails=false&maxThumbnailCount=999&filterValue=All&state=All&adminUnit=";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		
		String json = "";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.postForEntity(endPoint, requestEntity, String.class);
			if(response.getStatusCodeValue()==200) {
				JSONObject searchPj = JSONObject.parseObject(response.getBody());
				adminUid = searchPj.getJSONArray("items").getJSONObject(0).getString("objectId");
				System.out.println("Old AdminUid:"+adminUid);
			}
			else {
				System.out.println(response.getBody());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return adminUid;
	}
	
	private boolean deleteUser(String oldAdminUid) {
		boolean res = false;
		
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Users/"+oldAdminUid;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		
		String json = "";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.exchange(endPoint, HttpMethod.DELETE, requestEntity, String.class);
			System.out.println(response.getStatusCodeValue()+"|"+response.getBody());
			if(response.getStatusCodeValue()==200) {
				res = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private boolean permanentDeleteUser(String oldAdminUid) {
		boolean res = false;
		
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Users/PermanentDelete";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "[\""+oldAdminUid+"\"]";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.exchange(endPoint, HttpMethod.DELETE, requestEntity, String.class);
			System.out.println(response.getStatusCodeValue()+"|"+response.getBody());
			if(response.getStatusCodeValue()==200) {
				res = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
} 