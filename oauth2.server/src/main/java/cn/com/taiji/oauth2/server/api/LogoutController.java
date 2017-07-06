package cn.com.taiji.oauth2.server.api;

import java.lang.invoke.MethodHandles;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.com.taiji.oauth2.server.security.SsoAuthProvider;

@RestController
@SessionAttributes("authorizationRequest")
public class LogoutController {
	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;
	@Autowired
	SsoAuthProvider auth;
	static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private DefaultTokenServices defaultTokenServices;
	

	/**
	 * 
	 * @Description: 撤销令牌
	 * @param request
	 * @throws
	 * @author chixue
	 * @date 2017年3月30日
	 */
	@RequestMapping(value = "/revoke-token", method = RequestMethod.POST)
	public void logout(HttpServletRequest request,
			HttpServletResponse responsel) {
	String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			if(accessToken!=null&&accessToken.isExpired()){
				tokenStore.removeAccessToken(accessToken);
			}
		}
	}

	
	@GetMapping("/uaa/logout")
	public void logout(Principal principal, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
	    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)oAuth2Authentication.getDetails();
	    defaultTokenServices.revokeToken(details.getTokenValue());
	    String redirectUrl = getLocalContextPathUrl(request)+"/logout?myRedirect="+getRefererUrl(request);
	    LOGGER.debug("Redirect URL: {}",redirectUrl);
	    response.sendRedirect(redirectUrl);
	    return;
	}
	private String getRefererUrl(HttpServletRequest request) {
		return request.getHeader("referer");
	}

	private String getLocalContextPathUrl(HttpServletRequest request) {
		return request.getContextPath();
	}
}
