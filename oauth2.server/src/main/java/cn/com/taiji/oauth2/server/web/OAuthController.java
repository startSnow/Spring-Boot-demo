package cn.com.taiji.oauth2.server.web;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes("authorizationRequest")
public class OAuthController {
	
	@Autowired
	private TokenStore tokenStore;
	@Inject
	SessionRegistry sessionRegistry;
	
	@Autowired
	private DefaultTokenServices defaultTokenServices;
	
	/**
	 * 
	 * @Description: 确认
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception String  
	 * @throws
	 * @author chixue
	 * @date 2017年7月10日
	 */
	@RequestMapping("/oauth/confirm_access")
	public String getAccessConfirmation(Map<String, Object> model,
			HttpServletRequest request) throws Exception {
		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}
		return "auth";
	}
	/**
	 * 
	 * @Description: 
	 * @return String  
	 * @throws
	 * @author chixue
	 * @date 2017年7月10日
	 */
	@RequestMapping("/unauthenticated")
	public String unauthenticated() {
	  return "redirect:/?error=true";
	}
	
	@RequestMapping("/rediect")
	public  String rediect(
			HttpServletResponse responsel,String clientId,String token){
		      OAuth2Authentication authentication = tokenStore.readAuthentication(token);
		  	if (authentication == null) {
		  		throw new InvalidTokenException("Invalid access token: " + token);
		  	}
		  	OAuth2Request request=  	authentication.getOAuth2Request();
		  	Map map=new HashMap();
		  	map.put("code", request.getRequestParameters().get("code"));
		  	map.put("grant_type",  request.getRequestParameters().get("grant_type"));
		  	map.put("response_type", request.getRequestParameters().get("response_type"));
		  	if(clientId.equals("Beijing1")){
		  		map.put("redirect_uri", "http://10.0.32.15:9999/login");
		  	}else{
		  		map.put("redirect_uri", "http://10.0.26.13:9999/login");
		  	}
		  	map.put("client_id", clientId);
		  	map.put("state", request.getRequestParameters().get("state"));
		    request = new OAuth2Request(
		    		map,
		            clientId,
		            request.getAuthorities(),
		            request.isApproved(),
		            request.getScope()  ,
		            request.getResourceIds(),
		            map.get("redirect_uri").toString(),
		            request.getResponseTypes(),
		            request.getExtensions()
		            );
		      //模拟用户登录
		    	Authentication t= tokenStore.readAuthentication(token);
		        OAuth2Authentication auth=new OAuth2Authentication(request, t);
		        OAuth2AccessToken new_token=   defaultTokenServices.createAccessToken(auth);
		        return "redirect:/user_info?access_token="+new_token.getValue();
		    
		
	}
	
}