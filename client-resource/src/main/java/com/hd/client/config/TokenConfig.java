package com.hd.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class TokenConfig {
    /** JWT密钥 */
    private String signingKey = "fastboot";

    /**
     * JWT 令牌转换器
     * @return
     */
    @Bean("jwtAccessTokenConverter")
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws Exception {
        JwtAccessTokenConverter jwt = new MyJwtAccessTokenConverter();

        //        jwt.setSigningKey(signingKey);
        //        jwt.setVerifier(new MacSigner(signingKey));

        Resource resource = new ClassPathResource("public.txt");
        String publicKey = null;
        try {
            publicKey = inputStream2String(resource.getInputStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        jwt.setVerifierKey(publicKey);

        return jwt;
    }
    public   static   String   inputStream2String(InputStream is)   throws   Exception{
        ByteArrayOutputStream   baos   =   new ByteArrayOutputStream();
        int   i=-1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }

    /**
     * 配置 token 如何生成
     * 1. InMemoryTokenStore 基于内存存储
     * 2. JdbcTokenStore 基于数据库存储
     * 3. JwtTokenStore 使用 JWT 存储 该方式可以让资源服务器自己校验令牌的有效性而不必远程连接认证服务器再进行认证
     */
    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }
}

