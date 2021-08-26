package com.hd.auserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Setter
@Getter
public class UserInfo extends User {
    public UserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities){
        super(username, password,authorities);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loginTime= f.format(new Date());
    }
    String loginTime;
    String enterpriseId;
    String id;
}
