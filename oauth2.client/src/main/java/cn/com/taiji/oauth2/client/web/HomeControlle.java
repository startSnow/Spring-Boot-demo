package cn.com.taiji.oauth2.client.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 
 * 类名称：HomeControlle   
 * 类描述：   
 * 创建人：chixue   
 * 创建时间：2017年3月21日 下午4:23:34 
 * @version
 */
@Controller
public class HomeControlle {
	@RequestMapping({ "/", "/login" })
	  public String login(Principal user) {
	    return "index";
	  }

	@RequestMapping({ "/anonymous" })
	  public String anonymous(){
	    return "anonymous";
	  }
}
