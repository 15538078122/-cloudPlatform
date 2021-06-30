package com.hd.resourceserver.config;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能说明：权限决策处理类
 * 修改说明：
 * @Author: liwei
 * @date 2021-1-22 9:53:58
 * @version 0.1
 */
@Component
public class TheAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    private HttpServletRequest request; //自动注入request bean 代理模式

    private Map<String, List<String>> scopePermissions = new HashMap<String, List<String>>() {{
        put("write", new ArrayList() {{ add("/private/write/**");}});
        put("read", new ArrayList() {{ add("/private/read/**"); }});
    }};
    /**
     * permission 判断
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (authentication == null) {
            throw new AccessDeniedException("permission denied");
        }
        String uri=request.getRequestURI();
        Object principal =  authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).compareTo("anonymousUser") == 0) {
            //TODO: 判断是否是匿名允许的rui
            UrlMatcher matcher = new UrlMatcher("/public/**", "");
            if (matcher.matches(uri)){
                return;
            }
        } else {
            UserInfo userInfo = (UserInfo) authentication.getPrincipal();
            //判断当前user或scope具备的权限
            if (userInfo.getScopes().contains("user")) {
                //TODO: 依据用户权限判断
                return;
            } else {
                //TODO: 依据scope权限判断
                for (String scope : userInfo.getScopes()) {
                    List<String> uris = scopePermissions.get(scope);
                    for (String item : uris) {
                        UrlMatcher matcher = new UrlMatcher(item, "");
                        if (matcher.matches(uri)) {
                            return;
                        }
                    }
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

