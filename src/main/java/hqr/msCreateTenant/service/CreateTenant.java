package hqr.msCreateTenant.service;

import java.util.UUID;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hqr.msCreateTenant.domain.CommonObject;

@Service
public class CreateTenant {
	
	private RestTemplate restTemplate = new RestTemplate();	
	
	public CommonObject create(String token, String dirNm, String challengeId, String inputSolution, String location, String azureRegion) {
		CommonObject co = new CommonObject();
		String newTenantId = "";
		String endPoint = "https://main.iam.ad.ext.azure.com/api/Directories";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "{\"companyName\":\""+dirNm+"\",\n\"countryCode\":\""+location+"\",\n\"initialDomainPrefix\":\""+dirNm+"\",\"isTestTenant\":false,\n\"runIbizaCreateDirectoryLogic\":false,\n\"tenantRegionScope\":\"US\",\n\"testTenantRegionScope\":false,\n\"validateCaptchaRequest\":{\n\"azureRegion\":\""+azureRegion+"\",\n\"challengeType\":\"Visual\",\n\"testCaptchaRequest\":{\"challengeId\":\""+challengeId+"\",\n\"inputSolution\":\""+inputSolution+"\"}},\"checkAllowedToCreateTenants\": false}";
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		try {
			ResponseEntity<String> response= restTemplate.postForEntity(endPoint, requestEntity, String.class);
			JSONObject createTenantPj = JSON.parseObject(response.getBody());
			if(response.getStatusCodeValue()==200) {
				newTenantId = createTenantPj.getString("newTenantId");
				System.out.println("newTenantId:"+newTenantId);
				co.setNewTenantId(newTenantId);
				co.setDirName(dirNm);
			}
			else {
				System.out.println(createTenantPj);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return co;
	}

}
