package com.hd.client.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.*;

public class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {
    /**
     * 用户信息JWT
     */
    @Override
    protected Map<String, Object> decode(String token) {
        //解析请求当中的token  可以在解析后的map当中获取到上面加密的数据信息
        Map<String, Object> decode = super.decode(token);
        //先判断是否过期
        if(System.currentTimeMillis() > ((Long) decode.get("exp")*1000)){
            throw new InvalidTokenException("Invalid token (expired): " + token);
        }

        String userName = (String)decode.get("user_name");
        String loginTime = (String)decode.get("login_time");
        String clientId = (String)decode.get("client_id");
        String companyCode = (String)decode.get("company_code");

        List<String> scopes=new ArrayList<>();
        List<LinkedHashMap<String,String>> scopesArray =(List<LinkedHashMap<String,String>>) decode.get("scope");
        Iterator<LinkedHashMap<String,String>> it=scopesArray.iterator();
        while (it.hasNext()){
            Object temp1=it.next();
            String temp2 = (String) temp1;
            scopes.add(temp2);
        }

        List<GrantedAuthority> grantedAuthorityList=new ArrayList<>();
        List<LinkedHashMap<String,String>> authorities =(List<LinkedHashMap<String,String>>) decode.get("authorities");
        if(authorities!=null)
        {
            it=authorities.iterator();
            while (it.hasNext()){
                Object temp1=it.next();
                String temp2 = (String) temp1;
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(temp2);
                grantedAuthorityList.add(grantedAuthority);
            }
        }


        UserInfo userInfo =new UserInfo(userName,"", grantedAuthorityList);
        userInfo.setLoginTime(loginTime);
        userInfo.setScopes(scopes);
        userInfo.setCompanyCode(companyCode);
        //需要将解析出来的用户存入全局当中，不然无法转换成自定义的user类
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo,null, grantedAuthorityList);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //此处必须设置，encode时，会取出principle转化userinfo
        decode.put("user_name",userInfo);
        return decode;
    }
    public void  decodeJwtToken(String token){
        decode(token);
    }
}
