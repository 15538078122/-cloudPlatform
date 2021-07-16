package com.hd.microauservice.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyOrgVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyOrgEntity;
import com.hd.microauservice.service.SyOrgService;
import com.hd.microauservice.utils.VoConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public RetResult createOrg(@RequestBody SyOrgVo syOrgVo) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyOrgEntity syOrgEntity=new SyOrgEntity();
        VoConvertUtils.convertObject(syOrgVo.setEnterpriseId(tokenInfo.getCompanyCode()),syOrgEntity);
        syOrgService.save(syOrgEntity);
        return RetResponse.makeRsp("创建部门成功.");
    }
}