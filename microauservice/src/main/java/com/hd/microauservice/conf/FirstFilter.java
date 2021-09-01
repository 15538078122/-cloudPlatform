package com.hd.microauservice.conf;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.model.TokenInfo;
import com.hd.microauservice.entity.SyUserEntity;
import com.hd.microauservice.service.SyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug(((HttpServletRequest)request).getServletPath());
        String servletPath = ((HttpServletRequest) request).getServletPath();
        if(servletPath.indexOf("/auth")!=0 &&servletPath.indexOf("/swagger-ui.html")!=0
                &&servletPath.indexOf("/webjars")!=0 &&servletPath.indexOf("/static")!=0
                &&servletPath.indexOf("/null/swagger-resources")!=0&&servletPath.indexOf("/swagger-resources")!=0
                &&servletPath.indexOf("/v2")!=0 && servletPath.indexOf("/csrf")!=0
                &&servletPath.compareTo("/")!=0
                //&&false
        ){
            String tokenInfoJson=((HttpServletRequest)request).getHeader("token-info");
            log.debug("token-info: "+tokenInfoJson);
            TokenInfo tokenInfo= JSON.parseObject(tokenInfoJson,TokenInfo.class);
            QueryWrapper queryWrapper=new QueryWrapper(){{
                eq("account",tokenInfo.getAccount());
                eq("delete_flag",0);
                eq("enterprise_id",tokenInfo.getEnterpriseId());
            }};
            //SyUserEntity syUserEntity = syUserService.getOne(queryWrapper);
            SyUserEntity syUserEntity = syUserService.getOneFromCach(tokenInfo.getAccount(),tokenInfo.getEnterpriseId());
            //修改user id未业务系统的user id
            tokenInfo.setId(syUserEntity.getId().toString());
            SecurityContext.SetCurTokenInfo(tokenInfo);
        }

        //模拟server internal error,试验gateway retry功能设置
        //               StringBuffer sb=null;
        //                sb.append(1);
        chain.doFilter(request, response);
    }

}
