package cn.com.taiji.zuul;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
@Component
public class SimpleFilter extends ZuulFilter{
    private static final String HEADER="payload.trace";
	private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);
/*	pre:请求执行之前filter 
	route: 处理请求，进行路由 
	post: 请求处理完成后执行的filter 
	error:出现错误时执行的filter*/
	  @Override
	  public String filterType() {
	    return "pre";
	  }

	  @Override
	  public int filterOrder() {
	    return 1;
	  }

	  @Override
	  public boolean shouldFilter() {
	/*	   RequestContext ctx = RequestContext.getCurrentContext();
	        String requestUri = ctx.getRequest().getRequestURI();
	        System.out.println(requestUri);
	        return requestUri.startsWith("/oauth/**");*/
		  return true;
	  }

	  @Override
	  public Object run() {
	    RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();
        ctx.addZuulRequestHeader(HEADER, "true");
	    log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString(),request.getParameterMap()));
        RequestContext.getCurrentContext().set("javaPostFilter-ran", true);
	    return null;
	  }


}
