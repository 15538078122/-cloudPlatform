package com.hd.microsysservice.service.Impl;

import com.hd.common.model.TokenInfo;
import com.hd.microsysservice.conf.GeneralConfig;
import com.hd.microsysservice.service.AuthService;
import com.hd.microsysservice.service.SyFuncOpUrlService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.LicenseCheckUtil;
import com.hd.microsysservice.utils.UserCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

    @Autowired
    LicenseCheckUtil licenseCheckUtil;

    HashMap<String, List<String>> scopePermissionList = new HashMap<>();

    List permissionCodeList = new ArrayList<String>(
            Arrays.asList("micro:test1", "micro:test2", "menu:my", "enterprise:create")
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
        if (tokenInfo.getEnterpriseId().compareTo(GeneralConfig.ROOT_ENTERPRISE_ID) != 0 && tokenInfo.getAccount().compareTo(GeneralConfig.ENTERPRISE_ADMIN) != 0) {
            licenseCheckUtil.checkIfLicenseExpired(tokenInfo.getEnterpriseId());
        }
        //Thread.sleep(60);Thread.currentThread().getId()
        //scopes=scopes.toUpperCase();
        String scopes = tokenInfo.getScopes();
        List scopeList = Arrays.asList(scopes.split(","));
        String userId;
        String orgId;
        if (scopeList.contains("user")) {
            String UserInfoByCenterUserId = null;
            //SyUserEntity syUserEntity = syUserService.getUserByAccount(account,enterpriseId);
            if (tokenInfo.getId().isEmpty()) {
                //id orgid type
                UserInfoByCenterUserId = userCommonUtil.getUserIdByCenterUserIdFromCach(Long.parseLong(tokenInfo.getOauthId()));
                if (UserInfoByCenterUserId == null) {
                    return "-1";
                }
                //首次登录，需要判断类型
                String typeFlag = UserInfoByCenterUserId.substring(UserInfoByCenterUserId.lastIndexOf(':') + 1);
                if (typeFlag.compareTo("0") == 0 && tokenInfo.getDeviceType().compareTo("web") != 0) {
                    //return returnValue;
                } else if (typeFlag.compareTo("1") == 0 && tokenInfo.getDeviceType().compareTo("app") != 0) {
                    return "-1";
                }
                String[] strings = UserInfoByCenterUserId.split(":");
                userId = strings[0];
                orgId = strings[1];
            }
            else {
                //redis 缓存已存在，直接取
                userId = tokenInfo.getId();
                orgId = tokenInfo.getOrgId();
            }
        }
        else {
            userId = "0";
            orgId = "0";
        }
        String method = tokenInfo.getMethod();
        String uri = tokenInfo.getUri();
//        QueryWrapper<SyUrlMappingEntity> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("url", method.toLowerCase() + " " + uri);
//        queryWrapper.or().eq("url", "all " + uri);
//        SyUrlMappingEntity syUrlMappingEntity = syUrlMappingService.getOne(queryWrapper);
//        syUrlMappingEntity==null?null:syUrlMappingEntity.getPermCode();
        String permissionCode = syFuncOpUrlService.getPermissionCode(method.toLowerCase(), uri);
        String enterpriseId = tokenInfo.getEnterpriseId();
        String account = tokenInfo.getAccount();
        log.debug(String.format("check auth:\nenterpriseId:%s,user:%s,scope:%s,uri:%s,permCode:%s", enterpriseId, account, scopes
                , uri, permissionCode));
        if (permissionCode == null) {
            //不限制
            return String.format("%s:%s", userId, orgId);
        }
        //TODO: 具体的权限判断
        //sas模式 需要加入条件enterpriseId
        //首先判断scope
        if (scopeList.contains("user")) {
            List userPermissionList = syFuncOpUrlService.selectUserPerm(Long.parseLong(userId));
            //如果scope是user，使用用户的permission判断
            if (userPermissionList.contains(permissionCode)) {
                return String.format("%s:%s", userId, orgId);
            }
        }
        else {
            //根据scope 判断,不管user是谁
            for (Object scope : scopeList) {
                if ((scopePermissionList.get(scope) != null) && scopePermissionList.get(scope).contains(permissionCode)) {
                    return String.format("%s:%s", userId, orgId);
                }
            }
        }

        return  "-1";
    }
}
