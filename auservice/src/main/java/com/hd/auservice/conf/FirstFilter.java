package com.hd.auservice.conf;

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
        log.info(((HttpServletRequest)request).getRequestURL().toString());
        chain.doFilter(request, response);
    }

}
