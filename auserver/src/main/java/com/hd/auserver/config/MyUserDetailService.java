package com.hd.auserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public MyUserDetailService(){

    }
    @Autowired
    private HttpServletRequest request; //自动注入request bean 代理模式

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            List<GrantedAuthority> authList = getAuthorities();
            String companyCode="";
            //TODO: 获取用户密码 ，模拟试验，所有用户密码都是1234；
            //注意此处使用动态代理获取request对象，然后获取当初的请求参数；这里主要获取企业id，进行sas模式的用户管理
            String []objGrantType = request.getParameterMap().get("grant_type");
            if(objGrantType!=null && ((String)(objGrantType[0])).compareTo("password")==0){
                //密码模式，从request url获取company
                companyCode=(String)(request.getParameterMap().get("company")[0]);
            }else {
                //授权码显式或隐式模式，从defaultSavedRequest，之前存储的
                HttpSession session = request.getSession(true);
                Object objSavedReq = session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                if(objSavedReq!=null){
                    DefaultSavedRequest defaultSavedRequest=  (DefaultSavedRequest)objSavedReq;
                    companyCode = defaultSavedRequest.getParameterValues("company")[0];
                }
            }
            log.debug("companyCode--------"+companyCode);
            if(!companyCode.isEmpty()){
                //authList角色不使用
                userDetails = new UserInfo(userName, passwordEncoder.encode("1234"),authList);
                ((UserInfo)userDetails).setCompanyCode(companyCode);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        if(userDetails==null){
                throw new UsernameNotFoundException("缺少公司标识!");
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
