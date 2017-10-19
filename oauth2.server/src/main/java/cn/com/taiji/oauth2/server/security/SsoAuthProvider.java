package cn.com.taiji.oauth2.server.security;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
/**
 * 
 * 类名称：SsoAuthProvider   
 * 类描述：认证后转换系统用户   
 * 创建人：chixue   
 * 创建时间：2017年3月16日 上午10:48:13 
 * @version
 */
@Component
public class SsoAuthProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(SsoAuthProvider.class);
//TODO 缺少跟本地用户表匹对代码
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        System.out.println(username);
        
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), Collections.<GrantedAuthority>emptyList());
        
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}