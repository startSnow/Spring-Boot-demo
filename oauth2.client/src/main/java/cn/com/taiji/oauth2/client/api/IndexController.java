package cn.com.taiji.oauth2.client.api;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class IndexController {
	  @RequestMapping("/")
	  public String home(Principal user) {
	    return "Hello " ;
	  }

}
