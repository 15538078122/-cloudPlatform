package com.hd.microsysservice.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.QueryExpression;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyUpgradeVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.entity.SysUpgradeEntity;
import com.hd.microsysservice.service.SysUpgradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class SysUpgradeController extends SuperQueryController {

    @Autowired
    SysUpgradeService sysUpgradeService;

    @ApiOperation(value = "获取app版本信息")
    @RequiresPermissions(value = "version:appget",note = "获取app版本信息")
    @GetMapping("/app/version")
    public RetResult<SyUpgradeVo> getAppVersion() throws Exception {

        return RetResponse.makeRsp(sysUpgradeService.getAppVersion());
    }
    @ApiOperation(value = "分页获取app版本信息")
    @RequiresPermissions(value = "version:list",note = "分页获取app版本信息")
    @PostMapping("/app/version/list")
    public RetResult<MyPage> getAppVersionList(@RequestParam("query") String query) {
        PageQueryExpressionList pageQuery = JSON.parseObject(query, PageQueryExpressionList.class);
        Assert.isTrue(pageQuery!=null,"查询参数错误!");
        //adaptiveQueryColumn(pageQuery);
        QueryExpression queryExpression=new QueryExpression(){{
           setColumn("enterpriseId");
           setValue(SecurityContext.GetCurTokenInfo().getEnterpriseId());
           setType("eq");
        }};
        pageQuery.getQueryData().add(queryExpression);
        Page<SysUpgradeEntity> sysUpgradeEntityPage = selectPage(pageQuery, sysUpgradeService);
        SysUpgradeService.SyUpgradeVoConvertUtils syUpgradeVoConvertUtils=new SysUpgradeService.SyUpgradeVoConvertUtils();

        List<SyUpgradeVo> listVo = syUpgradeVoConvertUtils.convertToListT2(sysUpgradeEntityPage.getRecords());
//        List<SyUpgradeVo> listVo = new ArrayList<>();
//        for (SysUpgradeEntity sysUpgradeEntity : sysUpgradeEntityPage.getRecords()) {
//            SyUpgradeVo syUpgradeVo = new SyUpgradeVo();
//            VoConvertUtils.copyObjectProperties(sysUpgradeEntity, syEnterpriseVo);
//            listVo.add(syUpgradeVo);
//        }
        return RetResponse.makeRsp(new MyPage<>(sysUpgradeEntityPage.getCurrent(), sysUpgradeEntityPage.getSize(), sysUpgradeEntityPage.getTotal(), listVo));
    }
    @ApiOperation(value = "上传app版本信息")
    @RequiresPermissions(value = "version:appupload",note = "上传app版本信息")
    @PostMapping("/app/version")
    public RetResult<SyUpgradeVo> uploadAppVersion(@RequestBody @Validated SyUpgradeVo syUpgradeVo) throws Exception {
        sysUpgradeService.uploadAppVersion(syUpgradeVo);
        return RetResponse.makeRsp("更新版本成功");
    }
    @ApiOperation(value = "删除app版本信息")
    @RequiresPermissions(value = "version:appupload",note = "删除app版本信息")
    @DeleteMapping("/app/version")
    public RetResult delAppVersion(@RequestParam("id") Long id) throws Exception {
        sysUpgradeService.removeById(id);
        return RetResponse.makeRsp("删除版本成功");
    }
}

