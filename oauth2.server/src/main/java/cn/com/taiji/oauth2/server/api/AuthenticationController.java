package cn.com.taiji.oauth2.server.api;

import java.security.Principal;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.taiji.oauth2.server.service.TService;
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
	@Autowired
	TService tservice;
	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
		OAuth2Authentication auth=(OAuth2Authentication) principal;
	
		System.out.println("-------------------"+	auth.getName()+"-----"+	auth.getOAuth2Request().getClientId());
/*		System.out.println("当前登录用户的ip：" + auth.getRemoteAddress());
		System.out.println("当前登录用户的sessionID：" + auth.getSessionId()); //Principal可转换为User
*/

	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("name", principal.getName());
	  return map;
	}
	@RequestMapping({ "/client" })
	@ResponseBody
	public Map<String, String> user(String access_token) {
	  Map<String, String> map = new LinkedHashMap<>();
	 String clientId= tservice.getClientId(access_token);
	  map.put("clientId",clientId);
	  return map;
	}
	@RequestMapping({ "/userInfo" })
	@ResponseBody
	public Map<String, String> users(String access_token) {
		 Map<String, String> map = new LinkedHashMap<>();
		 String userName= tservice.getUserName(access_token.toString());
		  map.put("clientId",userName);
	  return map;
	}
	
}
