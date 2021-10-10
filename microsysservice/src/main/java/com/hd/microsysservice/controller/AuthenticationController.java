package com.hd.microsysservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.microsysservice.service.AuthFeignService;
import com.hd.microsysservice.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public RetResult auth(@RequestBody TokenInfo tokenInfo) throws InterruptedException {

        return RetResponse.makeRsp("登录成功",authService.auth(tokenInfo));
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
//    public RetResult authbridge(@RequestParam("account") String account, @RequestParam("scopes") String scopes, @RequestParam("uri") String uri
//            , @RequestParam("method") String method, @RequestParam("enterId") String enterpriseId) throws Exception {
    public RetResult authbridge(@RequestBody TokenInfo tokenInfo) throws Exception {
                //Thread.sleep(400);
        //        int i = 0;
        //        while (i++ < Integer.MAX_VALUE / 5) {
        //            i += i * (new Random()).nextInt();
        //        }

        RetResult retResult = authFeignService.auth(tokenInfo);

        return retResult;
    }
}