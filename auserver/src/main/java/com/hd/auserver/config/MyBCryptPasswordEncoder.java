package com.hd.auserver.config;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: liwei
 * @Description:
 */
public class MyBCryptPasswordEncoder extends BCryptPasswordEncoder {
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //TODO: 此处将前端加密的pwd转化为明文，根据需要调整
        String pwd1 = rawPassword.toString();
        String pwd2 = pwd1;
        return  super.matches(pwd2, encodedPassword);
    }
}
