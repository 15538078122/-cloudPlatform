package com.hd.resourceserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @Author: liwei
 * @Date 2021-01-08
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private AccessDecisionManager accessDecisionManager;

    @Autowired
    private FilterInvocationSecurityMetadataSource filterSecurityMetadataSource;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Since we want the protected resources to be accessible in the UI as well we need session creation to be allowed (it's disabled by default in 2.0.6)
        http
        //.anonymous().disable()  //匿名访问
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .requestMatchers().anyRequest()
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
//                .antMatchers("/private/read/**").access("#oauth2.hasScope('read1') and hasAnyRole('USER')")
//                .antMatchers("/private/write/**").access("#oauth2.hasScope('write1') and hasRole('ADMIN')")
//                .antMatchers("/private/**").authenticated()
//                .antMatchers("/public/**").permitAll()
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(accessDecisionManager);			//权限决策处理类
                        object.setSecurityMetadataSource(filterSecurityMetadataSource);	//路径（资源）拦截处理
                        return object;
                    }

                })
    ;

    }
    @Autowired
    private TokenStore tokenStore;
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }
//    @Primary
//    @Bean
//    public ResourceServerTokenServices tokenServices() {
//        final RemoteTokenServices tokenService = new RemoteTokenServices();
//        tokenService.setCheckTokenEndpointUrl(checkTokenAccess);
//        tokenService.setClientId(authorizationCodeResourceDetails.getClientId());
//        tokenService.setClientSecret(authorizationCodeResourceDetails.getClientSecret());
//        return tokenService;
//    }
}
