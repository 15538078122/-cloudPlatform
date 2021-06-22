package com.hd.auservice;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RefreshScope
@RestController
@Slf4j
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

    @RequestMapping("/auth")
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

    @RequestMapping("/authbridge")
    public RetResult authbridge(@RequestParam("account") String account, @RequestParam("scopes") String scopes, @RequestParam("uri") String uri, @RequestParam("method") String method) throws Exception {
        //int dd = 1 / 0;
        //Thread.sleep(400);
//        int i = 0;
//        while (i++ < Integer.MAX_VALUE / 5) {
//            i += i * (new Random()).nextInt();
//        }

        RetResult retResult = iAuthFeignService.auth(account,scopes,uri,method);

        return retResult;
    }
}