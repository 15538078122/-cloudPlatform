package com.hd.resourceserver.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

/**
 * 功能说明：路径拦截处理类
 * 修改说明：
 * @Author: liwei
 * @date 2021-1-22 9:52:27
 * @version 0.1
 */
@Component
public class TheFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    //定义角色的权限列表，实际应该从数据库取，这里为了简化程序先写死
    private Map<String, List<String>> rolePermissions = new HashMap<String, List<String>>() {{
        put("ROLE_ADMIN", new ArrayList() {{ add("/private/write");}});		//ADMIN角色有3个API的访问权限
        put("ROLE_UNUSE", new ArrayList() {{ add("/private/read"); }});										//USER只有/test/serverport这1个API的访问权限
    }};


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation)object;		//当前请求对象
        if (this.isMatcherAllowedRequest(fi)) {
            return null;		//return null 表示允许访问，不做拦截
        }
        List<ConfigAttribute> configAttributes = this.getMatcherConfigAttribute(fi.getRequestUrl());
        return configAttributes.size() > 0 ? configAttributes : this.deniedRequest();		//返回当前路径所需角色，如果没有则拒绝访问
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 功能说明：获取当前路径所需要的角色
     * 修改说明：
     * @Author: liwei
     * @date 2021-1-22 10:02:55
     * @param url 当前路径
     * @return 所需角色集合
     */
    private List<ConfigAttribute> getMatcherConfigAttribute(String url) {

        //有bug，不用这种模式，不想调整了
        List<ConfigAttribute> roles = new ArrayList<ConfigAttribute>();
        for (String role : this.rolePermissions.keySet()) {
            List<String> uriList = this.rolePermissions.get(role);
            for (String uri : uriList) {
                if (url.contains(uri)) {
                    roles.add(new SecurityConfig(role));
                    break;
                }
            }
        }

        return roles;
    }

    /**
     * 功能说明：判断当前请求是否在允许请求的范围内
     * 修改说明：
     * @Author: liwei
     * @date 2021-1-22 10:12:16
     * @param fi 当前请求
     * @return 是否再范围中
     */
    private boolean isMatcherAllowedRequest(FilterInvocation fi) {
        boolean result = this.allowedRequest().stream().map(AntPathRequestMatcher::new)
                .filter(requestMatcher -> requestMatcher.matches(fi.getHttpRequest()))
                .toArray().length > 0;
        return result;
    }

    /**
     * 功能说明：定义允许请求的列表
     * 修改说明：
     * @Author: liwei
     * @date 2021-1-22 10:09:56
     * @return
     */
    public List<String> allowedRequest() {
        return Arrays.asList("/login", "/hello");
    }

    /**
     * 功能说明：默认拒绝访问配置
     * 修改说明：
     * @Author: liwei
     * @date 2021-1-22 10:09:32
     * @return
     */
    public List<ConfigAttribute> deniedRequest() {
        return Collections.singletonList(new SecurityConfig("ROLE_DENIED"));		//默认需要的角色
    }
}

