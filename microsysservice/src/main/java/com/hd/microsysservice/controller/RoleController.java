package com.hd.microsysservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.KeyValuePair;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyRoleVo;
import com.hd.microsysservice.entity.SyRoleEntity;
import com.hd.microsysservice.service.SyRoleService;
import com.hd.microsysservice.utils.VerifyUtil;
import com.hd.microsysservice.utils.VoConvertUtils;
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
        VerifyUtil.verifyEnterId(syRoleVo.getEnterpriseId());
        syRoleService.createRole(syRoleVo);
        return RetResponse.makeRsp("创建角色成功.");
    }
    @ApiOperation(value = "编辑角色")
    @RequiresPermissions("role:edit")
    @PutMapping("/role/{id}")
    public RetResult editRole(@PathVariable("id") Long RoleId,@RequestBody @Validated SyRoleVo syRoleVo) throws Exception {
        VerifyUtil.verifyEnterId(syRoleVo.getEnterpriseId());
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
        VerifyUtil.verifyEnterId(syRoleService.getById(RoleId).getEnterpriseId());
        syRoleService.removeRoleId(RoleId);
        return RetResponse.makeRsp("删除角色成功");
    }
    @ApiOperation(value = "获取角色列表信息")
    @RequiresPermissions(value = "role:list",note = "分页获取角色列表")
    @PostMapping("/role/list")
    public RetResult getRoles(String query){
        PageQueryExpressionList pageQuery=VerifyUtil.verifyQueryParam(query,"enterpriseId","enterpriseId不能为空!");
        //PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        //Assert.isTrue(pageQuery!=null,"查询参数错误!");
        adaptiveQueryColumn(pageQuery);
//        QueryExpression queryExpression = pageQuery.getQueryExpressionByColumn("enterpriseId");
//        Assert.isTrue(queryExpression!=null,"enterpriseId不能为空!");
//        String enterId=queryExpression.getValue();
//        VerifyUtil.verifyEnterId(enterId);

//        QueryExpression queryExpression=new QueryExpression();
//        queryExpression.setColumn("enterpriseId");
//        queryExpression.setValue(enterId); //SecurityContext.GetCurTokenInfo().getenterpriseId()
//        queryExpression.setType("eq");
//        pageQuery.getQueryData().add(queryExpression);

        if(pageQuery.getOrderby().size()==0){
            pageQuery.getOrderby().add(new KeyValuePair("sortNum","asc"));
        }

        Page<SyRoleEntity> syRoleEntityPage= selectPage(pageQuery,syRoleService);
        List<SyRoleVo> listVo=new ArrayList<>();
        for(SyRoleEntity syRoleEntity : syRoleEntityPage.getRecords()){
            SyRoleVo syRoleVo=new SyRoleVo();
            VoConvertUtils.copyObjectProperties(syRoleEntity,syRoleVo);
            listVo.add(syRoleVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syRoleEntityPage.getCurrent(), syRoleEntityPage.getSize(), syRoleEntityPage.getTotal(), listVo));
    }
}