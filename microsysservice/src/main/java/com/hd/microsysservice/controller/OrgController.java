package com.hd.microsysservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyOrgVo;
import com.hd.common.vo.SyUserVo;
import com.hd.microsysservice.conf.operlog.OperLog;
import com.hd.microsysservice.service.SyOrgService;
import com.hd.microsysservice.utils.VerifyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liwei
 */
@Api(tags = "部门Controller")
@RestController
@Slf4j
public class OrgController  {
    @Autowired
    SyOrgService syOrgService;

    @ApiOperation(value = "创建部门")
    @RequiresPermissions("org:create")
    @PostMapping("/org")
    @OperLog(operModul = "部门管理",operType = "创建",operDesc = "新建部门")
    public RetResult createOrg(@RequestBody @Validated SyOrgVo syOrgVo) throws Exception {
        syOrgService.createOrg(syOrgVo);
        return RetResponse.makeRsp("创建部门成功.");
    }
    @ApiOperation(value = "获取部门tree")
    @RequiresPermissions("org:tree")
    @GetMapping("/org/tree")
    public RetResult getOrgTree(String enterId) {
        VerifyUtil.verifyEnterId(enterId);
        List<SyOrgVo> listVo = syOrgService.getOrgTree(enterId);
        return RetResponse.makeRsp(listVo);
    }

    @ApiOperation(value = "获取自己有权限的部门tree")
    @RequiresPermissions(value = "org:tree",note ="获取自己有权限的部门tree" )
    @GetMapping("/org/my/tree")
    public RetResult getOrgMyTree() {
        List<SyOrgVo> listVo = syOrgService.getMyOrgTree(false);
        return RetResponse.makeRsp(listVo);
    }
    @ApiOperation(value = "获取自己有权限的部门tree(含删除)")
    @RequiresPermissions(value = "org:treeall",note ="获取自己有权限的部门tree(含删除)" )
    @GetMapping("/org/my/treeall")
    public RetResult getOrgMyTreeall() {
        List<SyOrgVo> listVo = syOrgService.getMyOrgTree(true);
        return RetResponse.makeRsp(listVo);
    }
    @ApiOperation(value = "获取自己有权限的部门tree带人员")
    @RequiresPermissions(value = "org:treemen",note ="获取自己有权限的部门tree带人员" )
    @GetMapping("/org/my/treemen")
    public RetResult getOrgMyTreemen() {
        List<SyOrgVo> listVo = syOrgService.getMyOrgTreeWithMen();
        syOrgService.moveUserToChild(listVo);
        return RetResponse.makeRsp(listVo);
    }

    @ApiOperation(value = "获取自己有权限的部门人员")
    @RequiresPermissions(value = "org:men",note ="获取自己有权限的部门人员" )
    @GetMapping("/org/my/men")
    public RetResult getOrgMymen() {
        List<SyUserVo> listVo = syOrgService.getMyOrgMen();
        return RetResponse.makeRsp(listVo);
    }

    @ApiOperation(value = "编辑部门")
    @RequiresPermissions("org:edit")
    @PutMapping("/org/{id}")
    public RetResult editOrg(@PathVariable("id") Long orgId,@RequestBody @Validated SyOrgVo syOrgVo) throws Exception {
        syOrgVo.setId(orgId);
        syOrgService.updateOrg(syOrgVo);
        return RetResponse.makeRsp("修改部门成功");
    }
    @ApiOperation(value = "删除部门")
    @RequiresPermissions("org:delete")
    @DeleteMapping("/org/{id}")
    @OperLog(operModul = "部门管理",operType = "删除",operDesc = "删除部门")
    public RetResult delOrg(@PathVariable("id") Long orgId) throws Exception {
        syOrgService.delOrg(orgId);
        return RetResponse.makeRsp("删除部门成功");
    }
}