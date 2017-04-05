package cn.com.taiji.oauth2.client.conf;
/*package cn.com.taiji.conf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {
	@Autowired
	  UserInfoTokenServices tokenServces;
    org.springframework.security.oauth2.client.DefaultOAuth2ClientContext context;
    @Bean
    public DefaultTokenServices tokenServices() {
    	DefaultTokenServices token=	new DefaultTokenServices();
        return token;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("myResourceId");
        resources.tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
       // configure http security here...

        http.logout().logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler() {
                      @Override
                      public void onLogoutSuccess(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  Authentication authentication) {
                    	  
                          OAuth2AccessToken token = tokenServices().getAccessToken((OAuth2Authentication) authentication);
                          tokenServices().revokeToken(token.getValue());
                      }
                  });

    }
}*/