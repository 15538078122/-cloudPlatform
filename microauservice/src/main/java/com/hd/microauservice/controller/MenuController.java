package com.hd.microauservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyMenuEntity;
import com.hd.microauservice.service.FallbackFeignServiceDemo;
import com.hd.microauservice.service.SyMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @ApiOperation(value = "获取当前用户菜单")
    @RequiresPermissions("menu:my")
    @GetMapping("/menu/my")
    public RetResult getMyMenu() {
        log.debug(String.format("get menus for %s.",SecurityContext.GetCurTokenInfo().getAccount()));
        List<SyMenuEntity> list = syMenuService.list();

        return RetResponse.makeRsp(list);
    }
}