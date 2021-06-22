package com.hd.auserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.io.ByteArrayOutputStream;
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
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwt = new MyJwtAccessTokenConverter();
        ////rsa 设置公钥
        //        Resource resource = new ClassPathResource("public.txt");
        //        String publicKey = null;
        //        try {
        //            publicKey = inputStream2String(resource.getInputStream());
        //        } catch (Exception e) {
        //            throw new RuntimeException(e);
        //        }
        //        jwt.setVerifierKey(publicKey);

        //对称设置公钥
        //        jwt.setSigningKey(signingKey);
        //        jwt.setVerifier(new MacSigner(signingKey));

        //私钥设置
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        jwt.setKeyPair(keyStoreKeyFactory.getKeyPair("myjwt"));
        return jwt;
    }

    public   static   String   inputStream2String(InputStream is)   throws   Exception{
        ByteArrayOutputStream baos   =   new ByteArrayOutputStream();
        int   i=-1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }
    /**
     * 配置 token 如何生成
     * 1. InMemoryTokenStore 基于内存存储
     * 2. JdbcTokenStore 基于数据库存储
     * 3. JwtTokenStore 使用 JWT 存储 该方式可以让资源服务器自己校验令牌的有效性而不必远程连接认证服务器再进行认证
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
}

