package com.hd.auserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public MyUserDetailService(){

    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            List<GrantedAuthority> authList = getAuthorities();
            //TODO: 获取用户密码 ，模拟试验，所有用户密码都是1234；
            //authList角色不使用
            userDetails = new UserInfo(userName, passwordEncoder.encode("1234"),authList);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;
    }
//    增加前缀ROLE_，可以通过三种方式校验权限：
//    @PreAuthorize("hasRole('ADMIN')")                //允许
//    @PreAuthorize("hasRole('ROLE_ADMIN')")           //允许
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")      //允许
    private List<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authList;
    }
}
