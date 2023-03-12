package hqr.msCreateTenant.service;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GetDirName {

	private RestTemplate restTemplate = new RestTemplate();	
	
	public String getNewDirName(String token) {
		boolean isAva = false;
		String dirNm = "";
		while(!isAva) {
			dirNm = getNewWord();
			String endPoint = "https://main.iam.ad.ext.azure.com/api/Directories/DomainAvailability/" + dirNm;
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
			String body="";
			HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
			
			try {
				ResponseEntity<String> response= restTemplate.exchange(endPoint, HttpMethod.GET, requestEntity, String.class);
				String data = response.getBody();
				if("true".equals(data)) {
					System.out.println("DirName:"+dirNm+" is valid");
					isAva = true;
					break;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dirNm;
	}
	
	private String getNewWord() {
		SecureRandom sr = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		int length = sr.nextInt(3) + 6;
		for(int i=0; i<length; i++) {
			char typea = (char)(sr.nextInt(26)+97);
			sb.append(typea);
		}
		return sb.toString();
	}
}
