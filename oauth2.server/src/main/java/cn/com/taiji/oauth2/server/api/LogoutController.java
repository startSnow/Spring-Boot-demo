package cn.com.taiji.oauth2.server.api;

import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@SessionAttributes("authorizationRequest")
public class LogoutController {
	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;
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
	@RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.POST)
	public ResponseEntity logout(HttpServletRequest request,
			HttpServletResponse response) {
	String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			if (true) {
				HttpSession session = request.getSession(false);
				if (session != null)
					session.invalidate();
			}
			if (true) {
				SecurityContext context = SecurityContextHolder.getContext();
				context.setAuthentication(null);
			}
			SecurityContextHolder.clearContext();
			String tokenValue = authHeader.replace("bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
		}
		 return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping(value="/invalidateToken")
    public Map<String, String> logout(@RequestParam(name = "access_token") String accessToken) {
        LOGGER.debug("\n !!!!!!!!!! Invalidating token {} !!!!!!!!!!\n", accessToken);
        defaultTokenServices.revokeToken(accessToken);
        
        Map<String, String> ret = new HashMap<>();
        ret.put("access_token", accessToken);
        return ret;
    }
	
	@GetMapping("/uaa/logout")
	public void logout(Principal principal, HttpServletRequest request, HttpServletResponse response) throws Exception {

	    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
	    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)oAuth2Authentication.getDetails();
	    //OAuth2AccessToken accessToken = details.getAccessToken(oAuth2Authentication);
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
