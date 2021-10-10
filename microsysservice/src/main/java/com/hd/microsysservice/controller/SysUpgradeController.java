package com.hd.microsysservice.controller;


import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyUpgradeVo;
import com.hd.microsysservice.service.SysUpgradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wli
 * @since 2021-09-22
 */
@Api(tags = "系统升级Controller")
@RestController
@Slf4j
public class SysUpgradeController {

    @Autowired
    SysUpgradeService sysUpgradeService;

    @ApiOperation(value = "获取app版本信息")
    @RequiresPermissions(value = "version:appget",note = "获取app版本信息")
    @GetMapping("/app/version")
    public RetResult<SyUpgradeVo> getAppVersion() throws Exception {

        return RetResponse.makeRsp(sysUpgradeService.getAppVersion());
    }
    @ApiOperation(value = "上传app版本信息")
    @RequiresPermissions(value = "version:appupload",note = "上传app版本信息")
    @PostMapping("/app/version")
    public RetResult<SyUpgradeVo> uploadAppVersion(@RequestBody @Validated SyUpgradeVo syUpgradeVo) throws Exception {
        sysUpgradeService.uploadAppVersion(syUpgradeVo);
        return RetResponse.makeRsp("更新版本成功");
    }

}

