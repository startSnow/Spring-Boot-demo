package cn.com.taiji.oauth2.server.api;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	public static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	@Autowired
	private TokenStore tokenStore;
	/**
	 * 
	 * @Description: 返回用户对象
	 * @param principal
	 * @return Map<String,String>  
	 * @throws
	 * @author chixue
	 * @date 2017年7月14日
	 */
	@RequestMapping({ "/user", "/me" })
	public Map<String, String> user(Principal principal) {
	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("name", principal.getName());
	  return map;
	}
	//TODO 返回token 给子系统让他来请求用户的详细信息?
	/**
	 * 
	 * @Description: 为单点登录准备返回用户信息
	 * @param access_token
	 * @param response void  
	 * @throws
	 * @author chixue
	 * @date 2017年7月14日
	 */
	@RequestMapping({ "/user_info" })
	public void user(String access_token,HttpServletResponse response) {
		OAuth2Authentication auth=tokenStore.readAuthentication(access_token);
		OAuth2Request request=auth.getOAuth2Request();
	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("name", auth.getUserAuthentication().getName());
	  try {
		response.sendRedirect(request.getRedirectUri()+"?name="+auth.getUserAuthentication().getName());
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
/**
 * 
 * @Description: 根据token 返回用户的信息
 * @param access_token
 * @param response
 * @return Map  
 * @throws
 * @author chixue
 * @date 2017年7月14日
 */
	@RequestMapping(value = "/oauth/userInfo", method = RequestMethod.POST)
	public Map userInfo(String access_token,HttpServletResponse response) {
		OAuth2Authentication auth=tokenStore.readAuthentication(access_token);
	  Map<String, String> map = new LinkedHashMap<>();
	  map.put("name", auth.getUserAuthentication().getName());
	  map.put("openId", "1");
         return map;
	}

}
