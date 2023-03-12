package hqr.msCreateTenant.service;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hqr.msCreateTenant.domain.CommonObject;

@Service
public class GetChallengeCode {
	private RestTemplate restTemplate = new RestTemplate();
	
	public CommonObject getImgStr(String token) {
		CommonObject co = new CommonObject();
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Directories/CaptchaChallenge/Visual/en-us/";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());

		String body="";
		HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.exchange(endPoint, HttpMethod.GET, requestEntity, String.class);
			JSONObject challengePj = JSON.parseObject(response.getBody());
			String challengeId = challengePj.getString("challengeId");
			String challengeString = challengePj.getString("challengeString");
			String azureRegion = challengePj.getString("azureRegion");
			co.setToken(token);
			co.setChallengeId(challengeId);
			co.setChallengeString(challengeString);
			co.setAzureRegion(azureRegion);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return co;
	}

}
