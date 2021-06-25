package com.hd.auservice.controller;

import com.hd.auservice.IAuthFeignService;
import com.hd.auservice.model.RequiresPermissions;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "url权限验证Controller")
//@RefreshScope
@RestController
@Slf4j
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    IAuthFeignService iAuthFeignService;

    HashMap<String, List<String>> userPermissionList = new HashMap<>();
    HashMap<String, List<String>> scopePermissionList = new HashMap<>();
    List permissionList = new ArrayList<String>(
            Arrays.asList("get /test", "post /test", "delete /test")
    );

    public AuthenticationController() {
        //TODO: 权限从数据库取，放redis
        userPermissionList.put("liwei", permissionList);
        scopePermissionList.put("read1", permissionList);
        scopePermissionList.put("write1", permissionList);
    }

    @RequiresPermissions("permission:auth")
    @ApiOperation(value = "url权限验证func")
    @PostMapping(value = "/auth")
    public Boolean auth(@RequestParam("account") String account, @RequestParam("scopes") String scopes, @RequestParam("uri") String uri, @RequestParam("method") String method) throws InterruptedException {

        //Thread.sleep(60);
        log.info("check auth"+Thread.currentThread().getId());
        //scopes=scopes.toUpperCase();
        List scopeList =  Arrays.asList(scopes.split(","));

        //首先判断scope
        if (scopeList.contains("all")) {
            //判断该account是否具备访问uri的权限
            if ((userPermissionList.get(account)!=null) && userPermissionList.get(account).contains(method.toLowerCase() + " " + uri.toLowerCase())) {
                return true;
            }
        } else {
            //根据scope 判断,不管user是谁
            for(Object scope:scopeList){
                if((scopePermissionList.get(scope)!=null)&&scopePermissionList.get(scope).contains(method + " " + uri)){
                    return  true;
                }
            }
        }
        return false;
    }

    @RequiresPermissions("permission:authbr")
    @ApiOperation(value = "url权限验证bridge func")
    @PostMapping(value = "/authbridge")
    public RetResult authbridge(@RequestParam("account") String account, @RequestParam("scopes") String scopes, @RequestParam("uri") String uri, @RequestParam("method") String method) throws Exception {
//        int dd = 1 / 0;
        //Thread.sleep(400);
//        int i = 0;
//        while (i++ < Integer.MAX_VALUE / 5) {
//            i += i * (new Random()).nextInt();
//        }

        RetResult retResult = iAuthFeignService.auth(account,scopes,uri,method);

        return retResult;
    }
}