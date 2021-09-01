package com.hd.microauservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.QueryExpression;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyRoleVo;
import com.hd.microauservice.entity.SyRoleEntity;
import com.hd.microauservice.service.SyRoleService;
import com.hd.microauservice.utils.EnterpriseVerifyUtil;
import com.hd.microauservice.utils.VoConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei
 */
@Api(tags = "角色Controller")
@RestController
@Slf4j
public class RoleController extends SuperQueryController {
    @Autowired
    SyRoleService syRoleService;

    @ApiOperation(value = "创建角色")
    @RequiresPermissions("role:create")
    @PostMapping("/role")
    public RetResult createRole(@RequestBody @Validated SyRoleVo syRoleVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syRoleVo.getEnterpriseId());
        syRoleService.createRole(syRoleVo);
        return RetResponse.makeRsp("创建角色成功.");
    }
    @ApiOperation(value = "编辑角色")
    @RequiresPermissions("role:edit")
    @PutMapping("/role/{id}")
    public RetResult editRole(@PathVariable("id") Long RoleId,@RequestBody @Validated SyRoleVo syRoleVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syRoleVo.getEnterpriseId());
        syRoleService.updateRole(syRoleVo);
        return RetResponse.makeRsp("修改角色成功");
    }

    @ApiOperation(value = "获取角色详细")
    @RequiresPermissions(value = "role:detail",note = "获取角色详细")
    @GetMapping("/role/{id}")
    public RetResult getRoleDetail(@PathVariable("id") Long RoleId) throws Exception {
        SyRoleVo syRoleVo=syRoleService.getRoleDetail(RoleId);
        return RetResponse.makeRsp(syRoleVo);
    }

    @ApiOperation(value = "删除角色")
    @RequiresPermissions("role:delete")
    @DeleteMapping("/role/{id}")
    public RetResult delRole(@PathVariable("id") Long RoleId) {
        EnterpriseVerifyUtil.verifyEnterId(syRoleService.getById(RoleId).getEnterpriseId());
        syRoleService.removeRoleId(RoleId);
        return RetResponse.makeRsp("删除角色成功");
    }
    @ApiOperation(value = "获取角色列表信息")
    @RequiresPermissions(value = "role:list",note = "分页获取角色列表")
    @GetMapping("/role")
    public RetResult getRoles(String enterId,String query){
        EnterpriseVerifyUtil.verifyEnterId(enterId);
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        if(pageQuery==null) {
            pageQuery=new PageQueryExpressionList();
        }
        QueryExpression queryExpression=new QueryExpression();
        queryExpression.setColumn("enterpriseId");
        queryExpression.setValue(enterId); //SecurityContext.GetCurTokenInfo().getenterpriseId()
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);

        //pageQuery.getOrderby().add(new KeyValuePair("sort_num","asc"));

        //adaptiveQueryColumn(pageQuery);
        Page<SyRoleEntity> syRoleEntityPage= selectPage(pageQuery,syRoleService);
        List<SyRoleVo> listVo=new ArrayList<>();
        for(SyRoleEntity syRoleEntity : syRoleEntityPage.getRecords()){
            SyRoleVo syRoleVo=new SyRoleVo();
            VoConvertUtils.convertObject(syRoleEntity,syRoleVo);
            listVo.add(syRoleVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syRoleEntityPage.getCurrent(), syRoleEntityPage.getSize(), syRoleEntityPage.getTotal(), listVo));
    }
}