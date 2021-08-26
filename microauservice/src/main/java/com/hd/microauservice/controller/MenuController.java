package com.hd.microauservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyMenuBtnVo;
import com.hd.common.vo.SyMenuVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyMenuBtnEntity;
import com.hd.microauservice.entity.SyMenuEntity;
import com.hd.microauservice.service.SyMenuBtnService;
import com.hd.microauservice.service.SyMenuService;
import com.hd.microauservice.utils.EnterpriseVerifyUtil;
import com.hd.microauservice.utils.VoConvertUtils;
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
@Api(tags = "菜单Controller")
//@RefreshScope
@RestController
@Slf4j
public class MenuController {

    @Autowired
    SyMenuService syMenuService;
    @Autowired
    SyMenuBtnService syMenuBtnService;

    @ApiOperation(value = "获取当前企业菜单")
    @RequiresPermissions("menu:myenterprise")
    @GetMapping("/menu/myenterprise")
    public RetResult getMyEnterpriseMenu() throws Exception {
        List<SyMenuVo> listVo = syMenuService.getAllMenu(SecurityContext.GetCurTokenInfo().getEnterpriseId());
        return RetResponse.makeRsp(listVo);
    }

    @ApiOperation(value = "获取当前用户菜单")
    @RequiresPermissions("menu:my")
    @GetMapping("/menu/my")
    public RetResult getMyMenu() throws Exception {
//        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
//        if(tokenInfo==null){
//            throw  new Exception("没有当前用户!");
//        }
//        log.debug(String.format("get menus for %s.",tokenInfo.getAccount()));
        List<SyMenuVo> listVo = syMenuService.getCurrentUserMenu();
        //Thread.sleep(8000);
        return RetResponse.makeRsp(listVo);
    }

    @ApiOperation(value = "获取某个企业的菜单")
    @RequiresPermissions("enterprise:menu")
    @GetMapping("/menu")
    public RetResult getEnterpriseMenu(String enterId) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(enterId);
        List<SyMenuVo> listVo = syMenuService.getAllMenu(enterId);
        return RetResponse.makeRsp(listVo);
    }

    @ApiOperation(value = "创建菜单")
    @RequiresPermissions("menu:create")
    @PostMapping("/menu")
    public RetResult createMenu(@RequestBody  @Validated SyMenuVo syMenuVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syMenuVo.getEnterpriseId());
        //syMenuVo.setEnterpriseId(enterpriseId);
        syMenuService.createMenu(VoConvertUtils.syMenuToEntity(syMenuVo));
        return RetResponse.makeRsp("创建菜单成功");
    }

    @ApiOperation(value = "编辑菜单")
    @RequiresPermissions("menu:edit")
    @PutMapping("/menu/{id}")
    public RetResult editMenu(@PathVariable("id") Long menuId, @RequestBody @Validated SyMenuVo syMenuVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syMenuVo.getEnterpriseId());
        //syMenuVo.setEnterpriseId(SecurityContext.GetCurTokenInfo().getenterpriseCode());
        syMenuService.update(VoConvertUtils.syMenuToEntity(syMenuVo));
        return RetResponse.makeRsp("修改菜单成功");
    }

    @ApiOperation(value = "获取单个菜单")
    @RequiresPermissions("menu:get")
    @GetMapping("/menu/{id}")
    public RetResult getMenu(@PathVariable("id") Long menuId) throws Exception {
        SyMenuEntity syMenuEntity = syMenuService.getById(menuId);
        return RetResponse.makeRsp(VoConvertUtils.syMenuToVo(syMenuEntity));
    }

    @ApiOperation(value = "删除菜单")
    @RequiresPermissions("menu:del")
    @DeleteMapping("/menu/{id}")
    public RetResult delMenu(@PathVariable("id") Long menuId) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syMenuService.getById(menuId).getEnterpriseId());
        syMenuService.deleteMenu(menuId);
        return RetResponse.makeRsp("删除菜单成功");
    }

    @ApiOperation(value = "创建按钮")
    @RequiresPermissions("menuBtn:create")
    @PostMapping("/menu/btn")
    public RetResult createMenuBtn(@RequestBody SyMenuBtnVo syMenuBtnVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syMenuBtnVo.getEnterpriseId());
        syMenuBtnService.save(VoConvertUtils.syMenuBtnToEntity(syMenuBtnVo));
        return RetResponse.makeRsp("创建按钮成功");
    }

    @ApiOperation(value = "编辑按钮")
    @RequiresPermissions("menuBtn:edit")
    @PutMapping("/menu/btn/{id}")
    public RetResult editMenuBtn(@PathVariable("id") Long menuId, @RequestBody SyMenuBtnVo syMenuBtnVo) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syMenuBtnVo.getEnterpriseId());
        SyMenuBtnEntity syMenuBtnEntity = VoConvertUtils.syMenuBtnToEntity(syMenuBtnVo);
        syMenuBtnService.updateById(syMenuBtnEntity);
        return RetResponse.makeRsp("修改按钮成功");
    }

    @ApiOperation(value = "删除按钮")
    @RequiresPermissions("menuBtn:del")
    @DeleteMapping("/menu/btn/{id}")
    public RetResult delMenuBtn(@PathVariable("id") Long menuBtnId) throws Exception {
        EnterpriseVerifyUtil.verifyEnterId(syMenuBtnService.getById(menuBtnId).getEnterpriseId());
        syMenuBtnService.removeById(menuBtnId);
        return RetResponse.makeRsp("删除按钮成功");
    }

}