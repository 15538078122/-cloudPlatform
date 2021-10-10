package com.hd.microsysservice.conf;

import com.alibaba.fastjson.JSON;
import com.hd.common.model.TokenInfo;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.UserCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: liwei
 * @Description:
 */
@Slf4j
@WebFilter(filterName = "firstFilter",urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE+100)

public class FirstFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Autowired
    SyUserService syUserService;

    @Autowired
    UserCommonUtil userCommonUtil;

    @Value("${server.servlet.context-path}")
    String servletContextPath;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug(((HttpServletRequest)request).getServletPath());
        String servletPath = ((HttpServletRequest) request).getServletPath();
        String contentType = ((HttpServletRequest) request).getHeader("content-type");
        RepeatedlyReadRequestWrapper  wrapper=null;
        if(contentType!=null&&contentType.compareTo("application/json")==0)
        {
            wrapper=new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
            System.out.println("bodyStr = " + new String(wrapper.getBody()) );
        }

        //servletPath = servletPath.replaceFirst(servletContextPath,"");
//        if(servletPath.indexOf("/auth")!=0 &&servletPath.indexOf("/swagger-ui.html")!=0
//                &&servletPath.indexOf("/webjars")!=0 &&servletPath.indexOf("/static")!=0
//                &&servletPath.indexOf("/null/swagger-resources")!=0&&servletPath.indexOf("/swagger-resources")!=0
//                &&servletPath.indexOf("/v2")!=0 && servletPath.indexOf("/csrf")!=0
//                &&servletPath.compareTo("/")!=0
//                //&&false
//        )
        {
            String tokenInfoJson=((HttpServletRequest)request).getHeader("token-info");
            if(tokenInfoJson!=null){
                log.debug("token-info: "+tokenInfoJson);
                TokenInfo tokenInfo= JSON.parseObject(tokenInfoJson,TokenInfo.class);
                //修改user id未业务系统的user id
                //tokenInfo.setId(userCommonUtil.getUserIdFromCach(Long.parseLong(tokenInfo.getId())).toString());
                SecurityContext.SetCurTokenInfo(tokenInfo);
            }
         }
        if(contentType!=null&&contentType.compareTo("application/json")==0){
            chain.doFilter(wrapper, response);
        }
        else {
            chain.doFilter(request, response);
        }

    }
}
