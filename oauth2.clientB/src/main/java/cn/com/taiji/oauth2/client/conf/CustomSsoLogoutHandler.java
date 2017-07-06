package cn.com.taiji.oauth2.client.conf;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("customSsoLogoutHandler")
public class CustomSsoLogoutHandler implements LogoutHandler {

	static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup()
			.lookupClass());
	String logoutUrl = "http://AUTH-SERVER/revoke-token";
	 @Autowired
	    RestTemplate restTemplate;
	@Override
	public void logout(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Authentication authentication) {
		Object details = authentication.getDetails();
	//	authentication.getPrincipal()
		if (details.getClass().isAssignableFrom(
				OAuth2AuthenticationDetails.class)) {
			String accessToken = ((OAuth2AuthenticationDetails) details)
					.getTokenValue();
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("access_token", accessToken);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "bearer " + accessToken);
			HttpEntity<String> request = new HttpEntity(params, headers);
			HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
			HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
			restTemplate.setMessageConverters(Arrays
					.asList(new HttpMessageConverter[] {
							formHttpMessageConverter,
							stringHttpMessageConverternew }));
			try {

		ResponseEntity<String> result = restTemplate.exchange(logoutUrl, HttpMethod.POST, request, String.class);
		authentication.setAuthenticated(false);
		SecurityContextHolder.clearContext();
//		httpServletResponse.sendRedirect("/");
			} catch (HttpClientErrorException e) {
				LOGGER.error(
						"HttpClientErrorException invalidating token with SSO authorization server. response.status code: {}, server URL: {}",
						e.getStatusCode(), logoutUrl);
			}catch (Exception e) {
			
			}
		}
	}
}
