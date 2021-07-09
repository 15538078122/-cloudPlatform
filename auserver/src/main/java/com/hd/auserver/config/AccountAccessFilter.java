package com.hd.auserver.config;

import lombok.extern.slf4j.Slf4j;
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
@WebFilter(filterName = "accountAccessFilter",urlPatterns = "/account/*")
@Order(Ordered.HIGHEST_PRECEDENCE+100)
public class AccountAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Value("${config.USER_OP_IDENTIFICATION}")
    String  USER_OP_IDENTIFICATION;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String userOpIdentification = ((HttpServletRequest) request).getHeader("USER_OP_IDENTIFICATION");
        if(userOpIdentification==null||userOpIdentification.compareTo(USER_OP_IDENTIFICATION)!=0){
            throw new ServletException("无权限！");
        }
        chain.doFilter(request, response);
    }

}
