package com.hd.microauservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyUserVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyUserEntity;
import com.hd.microauservice.service.SyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author liwei
 */
@Api(tags = "用户Controller")
@RestController
@Slf4j
public class UserController {
    @Autowired
    SyUserService syUserService;

    @ApiOperation(value = "获取当前用户信息")
    @RequiresPermissions("curUser:info")
    @GetMapping("/cur-user/info")
    public RetResult getCurrentUser() throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        if(tokenInfo==null){
            throw  new Exception("没有当前用户!");
        }
        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("account",tokenInfo.getAccount());
            eq("enterprise_id",tokenInfo.getCompanyCode());
            eq("delete_flag",0);
        }};
        SyUserEntity syUserEntity = syUserService.getOne(queryWrapper);
        return RetResponse.makeRsp(syUserEntity);
    }

    @ApiOperation(value = "创建用户")
    @RequiresPermissions("user:create")
    @PostMapping("/user")
    public RetResult createUser(@RequestBody @Validated SyUserVo syUserVo) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        syUserService.createUser(syUserVo.setEnterpriseId(tokenInfo.getCompanyCode()));
        return RetResponse.makeRsp("创建用户成功.");
    }
    @ApiOperation(value = "移除用户")
    @RequiresPermissions("user:delete")
    @DeleteMapping("/user/{account}")
    public RetResult removeUser(@PathVariable("account") String account) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();

        SyUserVo syUserVo=new SyUserVo(){{
            setEnterpriseId(tokenInfo.getCompanyCode());
            setAccount(account);
        }};
        syUserService.removeUser(syUserVo);
        return RetResponse.makeRsp("移除用户成功.");
    }

}