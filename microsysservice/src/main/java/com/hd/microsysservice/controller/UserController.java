package com.hd.microsysservice.controller;

import com.alibaba.fastjson.JSON;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.conf.operlog.OperLog;
import com.hd.microsysservice.service.SyOrgService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.VerifyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author liwei
 */
@Api(tags = "用户Controller")
@RestController
@Slf4j
public class UserController extends SuperQueryController {
    @Autowired
    SyUserService syUserService;
    @Autowired
    SyOrgService syOrgService;

    public UserController() {
        mapQueryCols.put("enterpriseId", "enterpriseId");
    }

    @ApiOperation(value = "获取当前用户信息")
    @RequiresPermissions("curUser:info")
    @GetMapping("/cur-user/info")
    @OperLog(operModul = "用户管理",operType = "登录",operDesc = "登录获取个人信息")
    public RetResult getCurrentUser() throws Exception {
        SyUserVo syUserVo=syUserService.getCurrentUser();
        return RetResponse.makeRsp(syUserVo);
    }

    @ApiOperation(value = "获取用户详情")
    @RequiresPermissions(value = "user:get", note = "获取用户详情")
    @GetMapping("/user/{id}")
    public RetResult getUserDetail(@PathVariable("id") String userId) throws Exception {
        SyUserVo syUserVo = syUserService.getUser(userId);
        return RetResponse.makeRsp(syUserVo);
    }

    @ApiOperation(value = "启用禁用用户")
    @RequiresPermissions(value = "user:enabled", note = "启用禁用用户")
    @PutMapping("/user/enabled")
    public RetResult userEnabled(String userId,Boolean enabled) throws Exception {
        syUserService.enableUser(userId,enabled);
        return RetResponse.makeRsp("设置成功");
    }

//    @GetMapping("/user/root")
//    public RetResult getUser(){
//
//        return RetResponse.makeRsp("1234");
//    }
//    @GetMapping("/{user}/root")
//    public RetResult getUser2(@PathVariable("user") String user){
//
//        return RetResponse.makeRsp("4321");
//    }

    @ApiOperation(value = "创建用户")
    @RequiresPermissions(value = "user:create", note = "创建用户")
    @PostMapping("/user")
    @OperLog(operModul = "用户管理",operType = "创建",operDesc = "创建用户")
    public RetResult createUser(@RequestBody @Validated SyUserVo syUserVo) throws Exception {
        VerifyUtil.verifyEnterId(syUserVo.getEnterpriseId());
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        syUserService.createUser(syUserVo);
        return RetResponse.makeRsp("创建用户成功.");
    }

    @ApiOperation(value = "编辑用户")
    @RequiresPermissions(value = "user:edit", note = "编辑用户")
    @PutMapping("/user")
    public RetResult editUser(@RequestBody @Validated SyUserVo syUserVo) throws Exception {
        syUserService.updateUser(syUserVo);
        return RetResponse.makeRsp("编辑用户成功.");
    }

    @ApiOperation(value = "修改密码")
    @RequiresPermissions("user:changepwd")
    @PutMapping("/user/chpwd")
    public RetResult changepwd(@RequestBody SyUserVo syUserVo) throws Exception {
        syUserService.changepwd(syUserVo);
        return RetResponse.makeRsp("修改用户密码成功.");
    }

    @ApiOperation(value = "重置密码")
    @RequiresPermissions("user:resetpwd")
    @PutMapping("/user/resetpwd")
    public RetResult resetpwd(String id) throws Exception {
        syUserService.resetpwd(Long.parseLong(id));
        return RetResponse.makeRsp("重置用户密码成功.");
    }

    @ApiOperation(value = "移除用户")
    @RequiresPermissions("user:delete")
    @DeleteMapping("/user/{id}")
    public RetResult removeUser(@PathVariable("id") String id) throws Exception {
        syUserService.removeUser(Long.parseLong(id));
        return RetResponse.makeRsp("移除用户成功.");
    }


//    @ApiOperation(value = "企业用户获取部门用户列表信息")
//    @RequiresPermissions(value = "orgUser:list",note = "分页获取部门用户列表信息")
//    @GetMapping("/org/{orgId}/user")
//    public RetResult getOrgUserList2(@PathVariable("orgId") String orgId,@RequestParam("query") String query){
//
//        return  null;
//    }

    @ApiOperation(value = "企业用户获取部门用户列表信息")
    @RequiresPermissions(value = "orgUser:list", note = "分页获取部门用户列表信息")
    @PostMapping("/org/user/list")
    public RetResult getOrgUserList(@RequestParam("query") String query) throws Exception {
        PageQueryExpressionList pageQuery = JSON.parseObject(query, PageQueryExpressionList.class);
        Assert.isTrue(pageQuery != null, "查询参数错误!");
        adaptiveQueryColumn(pageQuery);
        return RetResponse.makeRsp(syUserService.getOrgUserList(pageQuery,true));
    }

    @ApiOperation(value = "root获取部门用户列表信息")
    @RequiresPermissions(value = "orgUserForRoot:list", note = "root分页获取部门用户列表信息")
    @PostMapping("/root/org/user/list")
    public RetResult getRootOrgUserList(@RequestParam("query") String query) throws Exception {
        PageQueryExpressionList pageQuery = JSON.parseObject(query, PageQueryExpressionList.class);
        Assert.isTrue(pageQuery != null, "查询参数错误!");
        adaptiveQueryColumn(pageQuery);
        return RetResponse.makeRsp(syUserService.getOrgUserList(pageQuery,false));
    }

    @ApiOperation(value = "获取角色的人员信息")
    @RequiresPermissions(value = "userbyrole:list", note = "获取某些角色的人员信息")
    @PostMapping("/userbyrole/list")
    public RetResult userbyrole(@RequestParam("query") String query) {
        PageQueryExpressionList pageQueryExpressionList = VerifyUtil.verifyQueryParam(query, "role", "角色role不能为空!");
        MyPage<SyUserVo> syUserVoMyPage=syUserService.userbyrole(pageQueryExpressionList);

        return RetResponse.makeRsp(syUserVoMyPage);
    }
}