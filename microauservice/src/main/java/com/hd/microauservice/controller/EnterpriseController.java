package com.hd.microauservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyEnterpriseVo;
import com.hd.microauservice.entity.SyEnterpriseEntity;
import com.hd.microauservice.service.SyEnterpriseService;
import com.hd.microauservice.utils.VoConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liwei
 */
@Api(tags = "企业Controller")
@RestController
@Slf4j
public class EnterpriseController  extends SuperQueryController {
    @Autowired
    SyEnterpriseService syEnterpriseService;
    public EnterpriseController(){
        mapQueryCols.put("name","name");
    }

    @ApiOperation(value = "获取企业列表信息")
    @RequiresPermissions(value = "enterprise:list",note = "分页获取企业列表")
    @GetMapping("/enterprise")
    public RetResult getEnterprise(@RequestParam("query") String query){
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        adaptiveQueryColumn(pageQuery);
        Page<SyEnterpriseEntity> syEnterpriseEntityPage= selectPage(pageQuery,syEnterpriseService);
        return RetResponse.makeRsp(new MyPage<>(syEnterpriseEntityPage.getCurrent(), syEnterpriseEntityPage.getSize(), syEnterpriseEntityPage.getTotal(), syEnterpriseEntityPage.getRecords()));
    }
    @ApiOperation(value = "创建企业")
    @RequiresPermissions("enterprise:create")
    @PostMapping("/enterprise")
    public RetResult createEnterprise(@RequestBody SyEnterpriseVo syEnterpriseVo){
        SyEnterpriseEntity syEnterpriseEntity=new SyEnterpriseEntity();
        VoConvertUtils.convertObject(syEnterpriseVo,syEnterpriseEntity);
        syEnterpriseService.save(syEnterpriseEntity);
        return RetResponse.makeRsp("创建企业成功.");
    }
    @ApiOperation(value = "编辑企业")
    @RequiresPermissions("enterprise:edit")
    @PutMapping("/enterprise/{id}")
    public RetResult editEnterprise(@PathVariable("id") Long id, @RequestBody SyEnterpriseVo syEnterpriseVo){
        SyEnterpriseEntity syEnterpriseEntity=new SyEnterpriseEntity();
        VoConvertUtils.convertObject(syEnterpriseVo,syEnterpriseEntity);
        syEnterpriseService.updateById(syEnterpriseEntity);
        return RetResponse.makeRsp("编辑企业成功.");
    }
    @ApiOperation(value = "删除企业")
    @RequiresPermissions("enterprise:delete")
    @DeleteMapping("/enterprise/{id}")
    public RetResult deleteEnterprise(@PathVariable("id") Long id){
        syEnterpriseService.removeById(id);
        return RetResponse.makeRsp("删除企业成功.");
    }
}
