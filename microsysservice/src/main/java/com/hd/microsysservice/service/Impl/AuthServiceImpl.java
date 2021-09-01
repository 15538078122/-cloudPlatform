package com.hd.microsysservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.microsysservice.entity.SyUrlMappingEntity;
import com.hd.microsysservice.entity.SyUserEntity;
import com.hd.microsysservice.service.AuthService;
import com.hd.microsysservice.service.SyFuncOpUrlService;
import com.hd.microsysservice.service.SyUrlMappingService;
import com.hd.microsysservice.service.SyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    SyUrlMappingService syUrlMappingService;

    @Autowired
    SyFuncOpUrlService syFuncOpUrlService;

    @Autowired
    SyUserService syUserService;

    HashMap<String, List<String>> scopePermissionList = new HashMap<>();

    List permissionCodeList = new ArrayList<String>(
            Arrays.asList("micro:test1", "micro:test2","menu:my","enterprise:create")
    );
//    HashMap<String, String> uriCode = new HashMap<String, String>();
//
//    {
//        uriCode.put("get /test1", "micro:test1");
//        uriCode.put("get /test2", "micro:test2");
//    }

    public AuthServiceImpl() {
        //TODO: scope权限从数据库取，放redis
        scopePermissionList.put("read", permissionCodeList);
        scopePermissionList.put("write", permissionCodeList);
    }
    @Override
    public Boolean auth(String account, String scopes, String uri, String method, String enterpriseId) {
        if(true) {
            return true;
        }
        //Thread.sleep(60);Thread.currentThread().getId()
        //scopes=scopes.toUpperCase();
        List scopeList = Arrays.asList(scopes.split(","));
        QueryWrapper<SyUrlMappingEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("url", method.toLowerCase() + " " + uri);
        queryWrapper.or().eq("url", "all " + uri);
        SyUrlMappingEntity syUrlMappingEntity = syUrlMappingService.getOne(queryWrapper);
        String permissionCode=syUrlMappingEntity==null?null:syUrlMappingEntity.getPermCode();
        permissionCode = syUrlMappingService.getPermissionCode(method.toLowerCase(),uri);
        log.debug(String.format("check auth:\nenterpriseId:%s,user:%s,scope:%s\nuri:%s permCode:%s", enterpriseId, account, scopes
                                ,uri,permissionCode));
        if (permissionCode == null) {
            //不限制
            return true;
        }
        //TODO: 具体的权限判断
        //sas模式 需要加入条件enterpriseId
        //首先判断scope
        if (scopeList.contains("user")) {
            SyUserEntity syUserEntity = syUserService.getOneFromCach(account,enterpriseId);
            List userPermissionList = syFuncOpUrlService.selectUserPerm(syUserEntity.getId());
            //如果scope是user，使用用户的permission判断
            if (userPermissionList.contains(permissionCode)) {
                return true;
            }
        } else {
            //根据scope 判断,不管user是谁
            for (Object scope : scopeList) {
                if ((scopePermissionList.get(scope) != null) && scopePermissionList.get(scope).contains(permissionCode)) {
                    return true;
                }
            }
        }
        return false;
    }
}
