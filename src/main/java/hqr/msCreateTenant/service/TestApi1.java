package hqr.msCreateTenant.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TestApi1 {
	private RestTemplate restTemplate = new RestTemplate();
	private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public void executePost() {
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Users";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		headers.add("x-ms-client-session-id", UUID.randomUUID().toString());
		headers.add("x-ms-command-name", "Common - AutoBatch");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.postForEntity(endPoint, requestEntity, String.class);
			System.out.println(response.getBody());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void executeGet() {
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Users";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		headers.add("x-ms-client-session-id", UUID.randomUUID().toString());
		headers.add("x-ms-command-name", "Common - AutoBatch");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "";
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.exchange(endPoint, HttpMethod.GET, requestEntity, String.class);
			System.out.println(response.getBody());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
