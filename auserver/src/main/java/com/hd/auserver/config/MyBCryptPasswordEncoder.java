package com.hd.auserver.config;

import com.hd.common.utils.RSAEncrypt;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: liwei
 * @Description:
 */
@Slf4j
public class MyBCryptPasswordEncoder extends BCryptPasswordEncoder {
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String pwd1 = rawPassword.toString();
        String pwd2 = null;
        try {
            //TODO: 此处将前端加密的pwd转化为明文，临时测试
            pwd1=RsaEncodePwd(pwd1);
            pwd2 = RsaDecodePwd(pwd1);
        } catch (Exception e) {
           return false;
        }
        if(!super.matches(pwd2, encodedPassword)){
            log.debug("用户名或密码错误！");
            throw new BadCredentialsException("用户名或密码错误！");
            //return false;
        }
        return  true;
    }
    public String RsaDecodePwd(String cipher) throws Exception {
        cipher =  java.net.URLDecoder.decode(cipher, "UTF-8");
        byte[] res = RSAEncrypt.decrypt(GenRsaFileTask.rsaPrivateKey, Base64.decode(cipher));
        String plainText=new String(res);
        return  plainText;
    }
    public String RsaEncodePwd(String plainText) throws Exception {
        byte[] cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(GenRsaFileTask.rsaPublicKey),plainText.getBytes());
        String cipher= Base64.encode(cipherData);
        cipher = java.net.URLEncoder.encode(cipher, "UTF-8");
        return  cipher;
    }
}
