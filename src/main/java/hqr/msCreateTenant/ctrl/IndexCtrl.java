package hqr.msCreateTenant.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.msCreateTenant.domain.CommonObject;
import hqr.msCreateTenant.service.CreateTenant;
import hqr.msCreateTenant.service.CreateUser;
import hqr.msCreateTenant.service.DeleteAdminThread;
import hqr.msCreateTenant.service.GetChallengeCode;
import hqr.msCreateTenant.service.GetDirName;
import hqr.msCreateTenant.service.GetToken;
import hqr.msCreateTenant.service.TestApi1;

//allow all the CORS
@CrossOrigin(originPatterns = "*localhost*")
@Controller
public class IndexCtrl {
	
	@Autowired
	private GetToken getToken;
	
	@Autowired
	private GetChallengeCode getChallengeCode;
	
	@Autowired
	private CreateTenant createTenant;
	
	@Autowired
	private GetDirName getDirName;
	
	@Autowired
	private CreateUser createUser;
	
	@Autowired
	private ThreadPoolTaskExecutor threadpool;
	
	@Autowired
	private TestApi1 t1;
	
	//Vue version
	@RequestMapping(value = {"/","index","index.html"})
	public String goIndex() {
		return "index";
	}
	
	//Jquery Version
	@RequestMapping(value = {"/index2","index2.html"})
	public String goIndex2() {
		return "index2";
	}
	
	@ResponseBody
	@RequestMapping(value = "/startUp" ,method = RequestMethod.POST)
	public String startUp(@RequestParam String username, @RequestParam String password) {
		getToken.setUsername(username);
		getToken.setPassword(password);
		getToken.setTenantId("common");
		String token = getToken.getMsToken();
		return token;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getChallengeCd", method = RequestMethod.POST)
	public CommonObject getChallengeCode(String token) {
		CommonObject co = getChallengeCode.getImgStr(token);
		return co;
	}
	
	@ResponseBody
	@RequestMapping(value = "/createTenant", method = RequestMethod.POST)
	public CommonObject createTenant(String token, String challengeId, String inputSolution, String location, String azureRegion) {
		return createTenant.create(token, getDirName.getNewDirName(token), challengeId, inputSolution, location, azureRegion);
	}
	
	@ResponseBody
	@RequestMapping(value="/createUser", method = RequestMethod.POST )
	public String createUser(String username, String password, String tenantId, String dirName, String location, boolean deleteUser) {
		getToken.setUsername(username);
		getToken.setPassword(password);
		getToken.setTenantId(tenantId);
		String token = getToken.getMsToken();
		String adminUid = createUser.create(token, dirName, password, location);
		if(!"".equals(adminUid) && deleteUser ) {
			DeleteAdminThread dat = new DeleteAdminThread();
			dat.setNewUsername("admin@"+dirName+".onmicrosoft.com");
			dat.setPassword(password);
			dat.setUsername(username);
			dat.setGetToken(getToken);
			
			threadpool.execute(dat);
		}
		
		return adminUid;
	}
	
}
