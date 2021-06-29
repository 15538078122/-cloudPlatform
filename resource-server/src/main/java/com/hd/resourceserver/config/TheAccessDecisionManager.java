package com.hd.resourceserver.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 功能说明：权限决策处理类
 * 修改说明：
 * @Author: liwei
 * @date 2021-1-22 9:53:58
 * @version 0.1
 */
@Component
public class TheAccessDecisionManager implements AccessDecisionManager {

    /**
     * 决定
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (authentication == null) {
            throw new AccessDeniedException("permission denied");
        }
        //当前用户拥有的角色集合
        List<String> roleCodes = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        //访问路径所需要的角色集合
        List<String> configRoleCodes = configAttributes.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toList());
        for (String roleCode : roleCodes) {
            if (configRoleCodes.contains(roleCode)) {
                if(roleCode.compareTo("ROLE_ANONYMOUS")==0){
                    return;
                }
                UserInfo userInfo =  (UserInfo)authentication.getPrincipal();
                //scope 判断
                if(userInfo.getScopes().contains("write1")){
                    return;
                }
            }
        }
        throw new AccessDeniedException("permission denied");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return false;
    }

}

