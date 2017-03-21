package cn.com.taiji.oauth2.server.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
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
        log.debug("自定义provider调用");

        // 返回一个Token对象表示登陆成功
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), Collections.<GrantedAuthority>emptyList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}