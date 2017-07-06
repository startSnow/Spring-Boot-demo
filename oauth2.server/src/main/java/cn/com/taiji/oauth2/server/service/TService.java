package cn.com.taiji.oauth2.server.service;

import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

@Service
public class TService {
	@Autowired
	private TokenStore tokenStore;
	/**
	 * 
	 * @Description: 根据TOKEN获得clientId
	 * @param tokenValue
	 * @return String  
	 * @throws
	 * @author chixue
	 * @date 2017年6月27日
	 */
	public String getClientId(String tokenValue) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
		if (authentication == null) {
			throw new InvalidTokenException("Invalid access token: " + tokenValue);
		}
		OAuth2Request clientAuth = authentication.getOAuth2Request();
		if (clientAuth == null) {
			throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
		}
		return clientAuth.getClientId();
	}
	/**
	 * 
	 * @Description: 简要进行方法说明，并对基础数据类型的参数和返回值加以说明
	 * @param headers
	 * @return String  
	 * @throws
	 * @author chixue
	 * @date 2017年6月27日
	 */
	public String getUserName(String tokenValue) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
		if (authentication == null) {
			throw new InvalidTokenException("Invalid access token: " + tokenValue);
		}
		return authentication.getName();
	}
}
