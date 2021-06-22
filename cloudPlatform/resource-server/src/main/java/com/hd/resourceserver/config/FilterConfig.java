package com.hd.resourceserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private EuthFilter authFilter;
    @Autowired
    private ButhFilter buthFilter;

    @Bean
    public FilterRegistrationBean registerAuthFilter2() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(buthFilter);
        registration.addUrlPatterns("/*");
        registration.setName("buthFilter");
        registration.setOrder(-3);  //值越小，Filter越靠前。
        return registration;
    }

    @Bean
    public FilterRegistrationBean registerAuthFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authFilter);
        registration.addUrlPatterns("/*");
        registration.setName("cuthFilter");
        registration.setOrder(-1);  //值越小，Filter越靠前。
        return registration;
    }


    //如果有多个Filter，再写一个public FilterRegistrationBean registerOtherFilter(){...}即可。
}
