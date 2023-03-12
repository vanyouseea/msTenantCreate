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

@Service
public class CreateUser {
	
	private RestTemplate restTemplate = new RestTemplate();	

	public String create(String token, String dirName, String password, String location) {
		String adminUid = "";
		
		String endPoint = "https://main.iam.ad.ext.azure.com/api/UserDetails";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		headers.add("x-ms-client-request-id", UUID.randomUUID().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = "{\n"
				+ "  \"accountEnabled\": true,\n"
				+ "  \"displayName\": \"Admin\",\n"
				+ "  \"usageLocation\": \""+location+"\",\n"
				+ "  \"userPrincipalName\": \"admin@"+dirName+".onmicrosoft.com\",\n"
				+ "  \"passwordProfile\": {\n"
				+ "    \"password\": \""+password+"\",\n"
				+ "    \"forceChangePasswordNextLogin\": false\n"
				+ "  },\n"
				+ "  \"selectedGroupIds\": [],\n"
				+ "  \"selectedRoleIds\": [\n"
				+ "    \"62e90394-69f5-4237-9190-012177145e10\"\n"
				+ "  ]\n"
				+ "}";
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		
		
		try {
			ResponseEntity<String> response= restTemplate.postForEntity(endPoint, requestEntity, String.class);
			JSONObject createTenantPj = JSON.parseObject(response.getBody());
			if(response.getStatusCodeValue()==200) {
				JSONObject adminPj = JSONObject.parseObject(response.getBody());
				adminUid = adminPj.getString("objectId");
				System.out.println("AdminUid:"+adminUid);
			}
			else {
				System.out.println(createTenantPj);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return adminUid;
	}
	
}
