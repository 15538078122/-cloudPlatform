package com.hd.auserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class MyDefaultOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

    public MyDefaultOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }
    @Override
    public OAuth2Request createOAuth2Request(AuthorizationRequest request) {

        //approve 自定义不起作用，在此拦截，添加上授权scope； 都是以scope.开头的属性
        Collection<String> approvedScopes=new ArrayList<String>();
        Map<String, String> approvalParameters = request.getApprovalParameters();
        if(approvalParameters.size()>0){
            for(String key : approvalParameters.keySet()){
                if(key.indexOf("scope.")==0){
                    String v = approvalParameters.get(key);
                    if(v.toLowerCase().compareTo("true")==0){
                        approvedScopes.add(key.replace("scope.",""));
                    }
                }
            }
        }
        //使用approvalParameters，避免第一次刚登录时记录scope范围，approve页面才起作用
        if(approvalParameters.size()>0){
            request.setScope(approvedScopes);
            //没有授权任何一个scope，拒绝访问
            if(approvedScopes.size()==0){
                throw new AccessDeniedException("permission denied");
            }
        }

        OAuth2Request oAuth2Request=request.createOAuth2Request();
        return oAuth2Request;
    }
}
