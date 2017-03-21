package cn.com.taiji.oauth2.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * 类名称：LoginController   
 * 类描述：   
 * 创建人：chixue   
 * 创建时间：2017年3月16日 上午11:28:19 
 * @version
 */
@Controller
public class LoginController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
}
