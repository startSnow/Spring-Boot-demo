package cn.com.taiji;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import cn.com.taiji.oauth2.client.conf.MySsoLogoutHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SpringBootApplication
@EnableOAuth2Sso
public class ClientApplication extends WebSecurityConfigurerAdapter {
	public static void main(String[] args) {
		UserInfoTokenServices tokenServces;
		org.springframework.security.oauth2.client.DefaultOAuth2ClientContext context;
		SpringApplication.run(ClientApplication.class, args);
	}

	@Autowired
	private MySsoLogoutHandler mySsoLogoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		        .antMatchers("/anonymous").permitAll()  
				.anyRequest()
				.authenticated()
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.addLogoutHandler(mySsoLogoutHandler).deleteCookies("JSESSIONID").invalidateHttpSession(true)
				.logoutSuccessUrl("/anonymous")
				.and()
				.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
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

}