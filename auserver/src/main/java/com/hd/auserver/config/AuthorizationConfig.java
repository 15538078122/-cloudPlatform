package com.hd.auserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * @Author: liwei
 * @Date 2021-01-08
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
@DependsOn({"jwtAccessTokenConverter"})
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = "secret123";
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String IMPLICIT = "implicit";
    private static final String GRANT_TYPE = "client_credentials";
    //token 有效时间 2小时
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 2 * 60 * 60;
    //刷新token有效时间 3天
    private static final int REFRESH_TOKEN_VALIDITY_SECONDS = 3 * 24 * 60 * 60;

    @Qualifier("myUserDetailService")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    ClientDetailsService clientDetailsService;

    public AuthorizationConfig() {
    }

    /**
     * Springboot2.x需要配置密码加密，否则报错：Encoded password does not look like BCrypt
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new MyBCryptPasswordEncoder();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CLIENT_ID)
                .secret(passwordEncoder().encode(CLIENT_SECRET))
                .authorizedGrantTypes(AUTHORIZATION_CODE, GRANT_TYPE, REFRESH_TOKEN, GRANT_TYPE_PASSWORD, IMPLICIT)
                //.autoApprove(true) // 为true 则不会被重定向到授权的页面，也不需要手动给请求授权,直接自动授权成功返回code
                .scopes("read", "write","user")//user 代表gateway中使用当前用户的权限进行permission拦截；其它使用scope对应权限拦截
                //TODO: 根据实际需要修改认证反馈uri
                .redirectUris("http://localhost:8000/public/code", "http://localhost:8000/public/token")
                //token 时间秒
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS)
        //.and().withClient(CLIENT_ID2)
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
//              .checkTokenAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                //允许表单参数模式secret_id
                .allowFormAuthenticationForClients()
                // 密码加密编码器
                .passwordEncoder(passwordEncoder())
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                // 认证管理器 - 在密码模式必须配置
                .authenticationManager(authenticationManager)
                // 自定义校验用户service
                .userDetailsService(userDetailsService)
                .tokenServices(authorizationServerTokenServices())
                // 是否能重复使用 refresh_token
                //.reuseRefreshTokens(false)
                .requestFactory(new MyDefaultOAuth2RequestFactory(clientDetailsService));
        ;
        // 设置令牌增强 JWT 转换
        TokenEnhancerChain enhancer = new TokenEnhancerChain();
        enhancer.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        endpoints.tokenEnhancer(enhancer);
    }

    /**
     * 获取一个token服务对象（该对象描述了token有效期等信息）
     */
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        // 使用默认实现
        DefaultTokenServices defaultTokenServices = new MyDefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true); // 是否开启令牌刷新
        defaultTokenServices.setTokenStore(tokenStore);

        // 针对jwt令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        // 设置令牌有效时间（一般设置为2个小时）
        defaultTokenServices.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS); // access_token就是我们请求资源需要携带的令牌
        // 设置刷新令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS); // 3天

        return defaultTokenServices;
    }

}
