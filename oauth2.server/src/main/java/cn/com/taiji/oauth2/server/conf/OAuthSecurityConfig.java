package cn.com.taiji.oauth2.server.conf;

import java.security.KeyPair;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
/**
 * 
 * 类名称：OAuthSecurityConfig   
 * 类描述：JDBC方式认证过滤器重写
 * 创建人：chixue   
 * 创建时间：2017年3月16日 上午10:47:34 
 * @version
 */
	@Configuration
	@EnableAuthorizationServer
	public class OAuthSecurityConfig extends AuthorizationServerConfigurerAdapter {
	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private DataSource dataSource;
/*	    
		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("euistore.jks"), "euisecret".toCharArray())
					.getKeyPair("euistore");
			converter.setKeyPair(keyPair);
			return converter;
		}*/

	    @Bean
	    public TokenStore tokenStore() {
	        return new JdbcTokenStore(dataSource);
	    }


	    @Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        endpoints.authenticationManager(authenticationManager);
	        endpoints.tokenStore(tokenStore());
	        // 配置TokenServices参数
	        DefaultTokenServices tokenServices = new DefaultTokenServices();
	        tokenServices.setTokenStore(endpoints.getTokenStore());
	        tokenServices.setSupportRefreshToken(false);
	        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
	        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
	        tokenServices.setAccessTokenValiditySeconds( (int) TimeUnit.MINUTES.toSeconds(1)); // 30天
	        endpoints.tokenServices(tokenServices);

	    }


	    @Override
	    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	        //oauthServer.checkTokenAccess("isAuthenticated()");
	        oauthServer.checkTokenAccess("permitAll()");
	        oauthServer.allowFormAuthenticationForClients();
	    }

	    @Bean
	    public ClientDetailsService clientDetails() {
	        return new JdbcClientDetailsService(dataSource);
	    }

	    @Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	        clients.withClientDetails(clientDetails());
	/*        clients.inMemory()
	                .withClient("client")
	                .secret("secret")
	                .authorizedGrantTypes("authorization_code")
	                .scopes("app");*/
	    }
}
