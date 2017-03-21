package cn.com.taiji.oauth2.server.api;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * 类名称：AuthenticationController   
 * 类描述： 第三方认证用户信息提取
 * 创建人：chixue   
 * 创建时间：2017年3月16日 上午10:46:28 
 * @version
 */
@RestController
public class AuthenticationController {
	public static final Logger logger = LoggerFactory
			.getLogger(AuthenticationController.class);
	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("name", principal.getName());
	  return map;
	}
	
}
