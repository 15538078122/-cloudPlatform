package com.hd.resourceserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class FirstFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("start to auth request validate...111");
        HttpServletRequest req = (HttpServletRequest) request;
//        String token = req.getHeader("token");
//        if (token != null) {
//            //    :TODO check token
//            log.info("\n*********************************auth success");
//
//        } else {
//            log.error("\n*********************************auth failed");
//        }
        chain.doFilter(request, response);
    }
}
