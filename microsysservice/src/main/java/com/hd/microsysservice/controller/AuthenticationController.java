package com.hd.microsysservice.controller;

import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.microsysservice.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "url权限验证Controller")
//@RefreshScope
@RestController
@Slf4j
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    AuthFeignService authFeignService;

    @Autowired
    AuthService authService;

    @RequiresPermissions("permission:auth")
    @ApiOperation(value = "url权限验证func")
    @PostMapping(value = "/auth")
    public Boolean auth(@RequestParam("account") String account, @RequestParam("scopes") String scopes, @RequestParam("uri") String uri
            , @RequestParam("method") String method, @RequestParam("enterId") String enterpriseId) throws InterruptedException {

        return authService.auth(account,scopes,uri,method,enterpriseId);
    }

    /**
     * 从网关转接下请求，使权限判断也使用负载均衡
     *
     * @param account
     * @param scopes
     * @param uri
     * @param method
     * @param enterpriseId
     * @return
     * @throws Exception
     */
    @RequiresPermissions("permission:authbr")
    @ApiOperation(value = "url权限验证bridge func")
    @PostMapping(value = "/authbridge")
    public RetResult authbridge(@RequestParam("account") String account, @RequestParam("scopes") String scopes, @RequestParam("uri") String uri
            , @RequestParam("method") String method, @RequestParam("enterId") String enterpriseId) throws Exception {
        //        int dd = 1 / 0;
                //Thread.sleep(400);
        //        int i = 0;
        //        while (i++ < Integer.MAX_VALUE / 5) {
        //            i += i * (new Random()).nextInt();
        //        }

        RetResult retResult = authFeignService.auth(account, scopes, uri, method, enterpriseId);

        return retResult;
    }
}