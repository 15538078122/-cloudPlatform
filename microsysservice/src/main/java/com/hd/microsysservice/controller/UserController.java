package com.hd.microsysservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.QueryExpression;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SyOrgEntity;
import com.hd.microsysservice.entity.SyUserEntity;
import com.hd.microsysservice.service.SyOrgService;
import com.hd.microsysservice.service.SyUserService;
import com.hd.microsysservice.utils.VerifyUtil;
import com.hd.microsysservice.utils.VoConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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


    public UserController(){
        mapQueryCols.put("enterpriseId","enterpriseId");
    }

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
            eq("enterprise_id",tokenInfo.getEnterpriseId());
            eq("delete_flag",0);
        }};
       //SyUserEntity syUserEntity = syUserService.getOne(queryWrapper);
        SyUserEntity syUserEntity = syUserService.getUserByAccount(tokenInfo.getAccount(),tokenInfo.getEnterpriseId());
        SyUserVo syUserVo=new SyUserVo();
        VoConvertUtils.copyObjectProperties(syUserEntity,syUserVo);

        return RetResponse.makeRsp(syUserVo);
    }
    @ApiOperation(value = "获取用户详情")
    @RequiresPermissions(value ="user:get",note = "获取用户详情")
    @GetMapping("/user/{id}")
    public RetResult getUserDetail(@PathVariable("id") String userId) throws Exception {
        SyUserVo syUserVo = syUserService.getUser(userId);
        return RetResponse.makeRsp(syUserVo);
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
    @RequiresPermissions(value = "user:create",note =  "创建用户")
    @PostMapping("/user")
    public RetResult createUser(@RequestBody @Validated SyUserVo syUserVo) throws Exception {
        VerifyUtil.verifyEnterId(syUserVo.getEnterpriseId());
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        syUserService.createUser(syUserVo.setEnterpriseId(tokenInfo.getEnterpriseId()));
        return RetResponse.makeRsp("创建用户成功.");
    }
    @ApiOperation(value = "编辑用户")
    @RequiresPermissions(value ="user:edit",note = "编辑用户")
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
    @RequiresPermissions(value = "orgUser:list",note = "分页获取部门用户列表信息")
    @PostMapping("/org/user/list")
    public RetResult getOrgUserList(@RequestParam("query") String query) throws Exception {
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        adaptiveQueryColumn(pageQuery);
        Long orgId=Long.parseLong(pageQuery.getQueryExpressionByColumn("orgId").getValue());
        //判断该用户是否有对应的部门权限，根据数据权限设置
        List<SyOrgEntity> syOrgEntities = syOrgService.getMyOrgList();
        SyOrgEntity syOrgEntityFind=null;
        for(SyOrgEntity item:syOrgEntities){
            if(item.getId().equals(Long.valueOf(orgId))){
                syOrgEntityFind=item;
            }
        }
        if(syOrgEntityFind==null){
            throw new Exception("没有权限!");
        }

        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();

        QueryExpression queryExpression=new QueryExpression();
        queryExpression.setColumn("delete_flag");
        queryExpression.setValue("0");
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);

//        queryExpression=new QueryExpression();
//        queryExpression.setColumn("org_id");
//        queryExpression.setValue(orgId.toString());
//        queryExpression.setType("eq");
//        pageQuery.getQueryData().add(queryExpression);

//        queryExpression=new QueryExpression();
//        queryExpression.setColumn("enterprise_id");
//        queryExpression.setValue(tokenInfo.getEnterpriseId());
//        queryExpression.setType("eq");
//        pageQuery.getQueryData().add(queryExpression);

        Page<SyUserEntity> syUserEntityPage= selectPage(pageQuery,syUserService);
        List<SyUserVo> listVo=new ArrayList<>();
        for(SyUserEntity syUserEntity : syUserEntityPage.getRecords()){
            SyUserVo syUserVo=new SyUserVo();
            VoConvertUtils.copyObjectProperties(syUserEntity,syUserVo);
            listVo.add(syUserVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syUserEntityPage.getCurrent(), syUserEntityPage.getSize(), syUserEntityPage.getTotal(),listVo));
    }

    @ApiOperation(value = "root获取部门用户列表信息")
    @RequiresPermissions(value = "orgUserForRoot:list",note = "root分页获取部门用户列表信息")
    @PostMapping("/root/org/user/list")
    public RetResult getRootOrgUserList(@RequestParam("query") String query) throws Exception {
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        Assert.isTrue(pageQuery!=null,"查询参数错误!");
        adaptiveQueryColumn(pageQuery);
        QueryExpression queryExpression = pageQuery.getQueryExpressionByColumn("orgId");
        Assert.isTrue(queryExpression!=null,"查询参数错误!");
        Long orgId=Long.parseLong(queryExpression.getValue());

        queryExpression=new QueryExpression();
        queryExpression.setColumn("delete_flag");
        queryExpression.setValue("0");
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);

//        queryExpression=new QueryExpression();
//        queryExpression.setColumn("org_id");
//        queryExpression.setValue(orgId);
//        queryExpression.setType("eq");
//        pageQuery.getQueryData().add(queryExpression);

        Page<SyUserEntity> syUserEntityPage= selectPage(pageQuery,syUserService);
        List<SyUserVo> listVo=new ArrayList<>();
        for(SyUserEntity syUserEntity : syUserEntityPage.getRecords()){
            SyUserVo syUserVo=new SyUserVo();
            VoConvertUtils.copyObjectProperties(syUserEntity,syUserVo);
            listVo.add(syUserVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syUserEntityPage.getCurrent(), syUserEntityPage.getSize(), syUserEntityPage.getTotal(),listVo));
    }
}