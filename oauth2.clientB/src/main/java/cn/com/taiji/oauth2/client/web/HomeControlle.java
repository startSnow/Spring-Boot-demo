package cn.com.taiji.oauth2.client.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 
 * 类名称：HomeControlle   
 * 类描述：   
 * 创建人：chixue   
 * 创建时间：2017年3月21日 下午4:23:34 
 * @version
 */
@Controller
public class HomeControlle {
	@RequestMapping({ "/", "/login" })
	  public String login(Principal user) {
	    return "index";
	  }

	@RequestMapping({ "/anonymous" })
	  public String anonymous(){
	    return "anonymous";
	  }
	/**
	 * 
	 * @Description: 获取token
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param authentication
	 * @return String  
	 * @throws
	 * @author chixue
	 * @date 2017年6月26日
	 */
	@RequestMapping({ "/getToken" })
	@ResponseBody
	  public String anonymous(HttpServletRequest httpServletRequest,
				HttpServletResponse httpServletResponse,
				Authentication authentication){
		Object details = authentication.getDetails();
		String accessToken="";
			if (details.getClass().isAssignableFrom(
					OAuth2AuthenticationDetails.class)) {
				 accessToken = ((OAuth2AuthenticationDetails) details).getTokenValue();
			}
	    return accessToken;
	  }
	
}
