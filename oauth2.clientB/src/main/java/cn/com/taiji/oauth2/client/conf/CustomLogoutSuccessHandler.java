package cn.com.taiji.oauth2.client.conf;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
/**
 * 
 * 类名称：CustomLogoutSuccessHandler   
 * 类描述：   退出成功后事件
 * 创建人：chixue   
 * 创建时间：2017年4月10日 下午3:21:10 
 * @version
 */
@Component
@Qualifier("customLogoutSuccessHandler")
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) 
			throws IOException, ServletException {
		String redirectUrl = request.getContextPath() + "/anonymous";
		auth.setAuthenticated(false);
	response.sendRedirect(redirectUrl);
	}
}