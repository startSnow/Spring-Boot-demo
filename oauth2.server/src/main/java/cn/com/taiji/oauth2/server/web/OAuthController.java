package cn.com.taiji.oauth2.server.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("authorizationRequest")
public class OAuthController {

	@RequestMapping("/oauth/confirm_access")
	public String getAccessConfirmation(Map<String, Object> model,
			HttpServletRequest request) throws Exception {
		if (request.getAttribute("_csrf") != null) {
			model.put("_csrf", request.getAttribute("_csrf"));
		}

		return "auth";
	}
	
	


}