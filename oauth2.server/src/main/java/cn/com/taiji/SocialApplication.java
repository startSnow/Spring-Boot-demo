package cn.com.taiji;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

/**
 * - - 类名称：SocialApplication - 类描述：核心启动类 - 创建人：chixue - 创建时间：2017年3月16日
 * 上午10:50:07 - @version
 */
@SpringBootApplication
@EnableAuthorizationServer
@EnableDiscoveryClient
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SocialApplication extends WebSecurityConfigurerAdapter {
	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
	}

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	Environment environment;

	@Bean
	@Primary
	public ObjectMapper jacksonObjectMapper() {
		// @formatter:off
		return new ObjectMapper()
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
						false)
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.setDateFormat(new ISO8601DateFormat());

		/*
		 * .registerModule(new JodaModule()) // add more Module
		 * 
		 * .registerModule(new CmsModule()) .registerModule(new PortalModule())
		 * .registerModule(new SysModule());
		 */

		// @formatter:on
	}

	@Bean
	public MappingJackson2JsonView mappingJackson2JsonView() {
		MappingJackson2JsonView v = new org.springframework.web.servlet.view.json.MappingJackson2JsonView();
		v.setObjectMapper(jacksonObjectMapper());
		v.setPrettyPrint(true);
		return v;
	}

	protected class MappingJackson2JsonpView extends MappingJackson2JsonView {
		public static final String DEFAULT_CONTENT_TYPE = "application/javascript";

		@Override
		public String getContentType() {
			return DEFAULT_CONTENT_TYPE;
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			Map<String, String[]> params = request.getParameterMap();
			if (params.containsKey("callback")) {
				response.getOutputStream().write(
						new String(params.get("callback")[0] + "(").getBytes());
				super.render(model, request, response);
				response.getOutputStream().write(new String(");").getBytes());
				response.setContentType(DEFAULT_CONTENT_TYPE);
			} else {
				super.render(model, request, response);
			}
		}
	}

	@Bean
	public MappingJackson2JsonpView mappingJackson2JsonpView() {
		MappingJackson2JsonpView v = new MappingJackson2JsonpView();
		v.setObjectMapper(jacksonObjectMapper());
		v.setPrettyPrint(false);
		return v;
	}

	// @Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer
				.favorParameter(true)
				.ignoreAcceptHeader(false)
				.defaultContentType(MediaType.TEXT_HTML)
				.mediaType("json", MediaType.APPLICATION_JSON)
				.mediaType("jsonp", MediaType.valueOf("application/javascript"));
	}

	@Bean
	public ViewResolver contentNegotiatingViewResolver(
			ContentNegotiationManager manager) {
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
		resolvers.add(thymeleafViewResolver);
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setViewResolvers(resolvers);
		resolver.setContentNegotiationManager(manager);

		List<View> views = new ArrayList<View>();
		views.add(mappingJackson2JsonView());
		views.add(mappingJackson2JsonpView());
		resolver.setDefaultViews(views);
		return resolver;

	}

	// see
	@Bean
	public Collection<IDialect> dialects() {
		Collection<IDialect> dialects = new HashSet<IDialect>();
		dialects.add(new org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect());
		return dialects;
	}
	


	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(authenticationProvider));
	}
	    
	@Bean
	public SessionRegistry sessionRegistry(){    
	    return new SessionRegistryImpl();    
	}
	@Configuration
	public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/login").setViewName("login");
	    }
	}

	@Override  
    protected void configure(HttpSecurity http) throws Exception {  
                 http
 				.formLogin().loginPage("/login").permitAll()
 			.and()
 				.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
 			.and()
 				.authorizeRequests().anyRequest().authenticated()
                 .and().sessionManagement().maximumSessions(2).expiredUrl("/login?expired").sessionRegistry(sessionRegistry());  
    }  
	
	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends
			ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/me").authorizeRequests().anyRequest()
					.authenticated();
		}
	}
	/**
	 * 
	 * @Description: Generating a 401 in the Server
	 * @param template
	 * @return AuthoritiesExtractor  
	 * @throws
	 * @author chixue
	 * @date 2017年4月5日
	 */
	@Bean
	public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
	  return map -> {
	    String url = (String) map.get("organizations_url");
	    @SuppressWarnings("unchecked")
	    List<Map<String, Object>> orgs = template.getForObject(url, List.class);
	    if (orgs.stream()
	        .anyMatch(org -> "spring-projects".equals(org.get("login")))) {
	      return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
	    }
	    throw new BadCredentialsException("Not in Spring Projects origanization");
	  };
	}
	@Bean
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new OAuth2RestTemplate(resource, context);
	}
}
