package com.hd.microauservice.conf;

import com.alibaba.fastjson.JSON;
import com.hd.common.model.TokenInfo;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug(((HttpServletRequest)request).getServletPath());
        String servletPath = ((HttpServletRequest) request).getServletPath();
        if(servletPath.indexOf("/auth")!=0){
            String tokenInfoJson=((HttpServletRequest)request).getHeader("token-info");
            log.debug("token-info: "+tokenInfoJson);
            TokenInfo tokenInfo= JSON.parseObject(tokenInfoJson,TokenInfo.class);
            SecurityContext.SetCurTokenInfo(tokenInfo);
        }

        //模拟server internal error,试验gateway retry功能设置
        //               StringBuffer sb=null;
        //                sb.append(1);
        chain.doFilter(request, response);
    }

}
