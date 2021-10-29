package com.hd.microsysservice.conf;

/**
 * @Author: liwei
 * @Description:
 */

import com.alibaba.fastjson.JSON;
import com.hd.common.utils.RSAEncrypt;
import com.hd.microsysservice.utils.JwtUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FeignInterceptor implements RequestInterceptor{

    @Value("${config.USER_OP_IDENTIFICATION}")
    String USER_OP_IDENTIFICATION;

    @Autowired
    HttpServletRequest request;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void apply(RequestTemplate requestTemplate){
         String url = requestTemplate.url();
         if(url.indexOf("/account")==0) {
             // 获取request请求中的Header信息放到requestTemplate header里
             SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             byte[] cipherData = new byte[0];
             try {
                 cipherData = RSAEncrypt.encrypt(jwtUtils.rsaPublicKey, (USER_OP_IDENTIFICATION + f.format(new Date())).getBytes());
             } catch (Exception e) {
                 e.printStackTrace();
             }
             String userOpIdentificationEncode = Base64.encode(cipherData);
             try {
                 userOpIdentificationEncode = java.net.URLEncoder.encode(userOpIdentificationEncode, "UTF-8");
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }
             requestTemplate.header("USER_OP_IDENTIFICATION", userOpIdentificationEncode);
         }
         else {
             //添加tokeninfo
             String tokenInfoJson= JSON.toJSONString(SecurityContext.GetCurTokenInfo());
             requestTemplate.header("token-info", tokenInfoJson);
         }
    }
}

