package com.hd.microsysservice.service.Impl;

import com.hd.common.model.TokenInfo;
import com.hd.microsysservice.service.AuthService;
import com.hd.microsysservice.service.SyFuncOpUrlService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.UserCommonUtil;
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
    SyFuncOpUrlService syFuncOpUrlService;

    @Autowired
    SyUserService syUserService;

    @Autowired
    UserCommonUtil userCommonUtil;

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
    /**
     * 返回：失败-1；成功：userId:orgId，如果是client id登录，返回0:0；
     */
    public String auth(TokenInfo tokenInfo) {
        //Thread.sleep(60);Thread.currentThread().getId()
        //scopes=scopes.toUpperCase();
        String scopes=tokenInfo.getScopes();
        List scopeList = Arrays.asList(scopes.split(","));
        String returnValue="-1";
        if (scopeList.contains("user")) {
            String UserIdByCenterUserId = userCommonUtil.getUserIdByCenterUserIdFromCach(Long.parseLong(tokenInfo.getId()));
            //SyUserEntity syUserEntity = syUserService.getUserByAccount(account,enterpriseId);

            if(UserIdByCenterUserId==null){
                return returnValue;
            }
            //判断类型
            String typeFlag=UserIdByCenterUserId.substring(UserIdByCenterUserId.lastIndexOf(':')+1);
            if(typeFlag.compareTo("0")==0 && tokenInfo.getDeviceType().compareTo("web")!=0){
                return returnValue;
            }
            else if(typeFlag.compareTo("1")==0 && tokenInfo.getDeviceType().compareTo("app")!=0){
                return returnValue;
            }
            UserIdByCenterUserId=UserIdByCenterUserId.substring(0,UserIdByCenterUserId.lastIndexOf(':'));

            returnValue=UserIdByCenterUserId;
            if(true){
                return returnValue;
            }
        }
        else {
            returnValue = "0:0";
        }
        String method=tokenInfo.getMethod();
        String uri=tokenInfo.getUri();
//        QueryWrapper<SyUrlMappingEntity> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("url", method.toLowerCase() + " " + uri);
//        queryWrapper.or().eq("url", "all " + uri);
//        SyUrlMappingEntity syUrlMappingEntity = syUrlMappingService.getOne(queryWrapper);
//        syUrlMappingEntity==null?null:syUrlMappingEntity.getPermCode();
        String permissionCode= syFuncOpUrlService.getPermissionCode(method.toLowerCase(),uri);
        String enterpriseId=tokenInfo.getEnterpriseId();
        String account=tokenInfo.getAccount();
        log.debug(String.format("check auth:\nenterpriseId:%s,user:%s,scope:%s,uri:%s,permCode:%s", enterpriseId, account, scopes
                                ,uri,permissionCode));
        if (permissionCode == null) {
            //不限制
            return returnValue;
        }
        //TODO: 具体的权限判断
        //sas模式 需要加入条件enterpriseId
        //首先判断scope
        if (scopeList.contains("user")) {
            List userPermissionList = syFuncOpUrlService.selectUserPerm(Long.parseLong(returnValue.split(":")[0]));
            //如果scope是user，使用用户的permission判断
            if (userPermissionList.contains(permissionCode)) {
                return returnValue;
            }
        } else {
            //根据scope 判断,不管user是谁
            for (Object scope : scopeList) {
                if ((scopePermissionList.get(scope) != null) && scopePermissionList.get(scope).contains(permissionCode)) {
                    return returnValue;
                }
            }
        }
        returnValue="-1";
        return returnValue;
    }
}
