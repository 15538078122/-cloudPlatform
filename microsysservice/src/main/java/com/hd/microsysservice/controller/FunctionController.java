package com.hd.microsysservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyFuncOpUrlVo;
import com.hd.common.vo.SyFuncOperatorVo;
import com.hd.common.vo.SyFunctionVo;
import com.hd.common.vo.SyUrlMappingVo;
import com.hd.microsysservice.entity.SyFuncOpUrlEntity;
import com.hd.microsysservice.entity.SyFuncOperatorEntity;
import com.hd.microsysservice.entity.SyFunctionEntity;
import com.hd.microsysservice.entity.SyUrlMappingEntity;
import com.hd.microsysservice.service.SyFuncOpUrlService;
import com.hd.microsysservice.service.SyFuncOperatorService;
import com.hd.microsysservice.service.SyFunctionService;
import com.hd.microsysservice.service.SyUrlMappingService;
import com.hd.microsysservice.utils.VoConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei
 */
@Api(tags = "功能Controller")
//@RefreshScope
@RestController
@Slf4j
public class FunctionController  extends SuperQueryController {

    @Autowired
    SyFunctionService  syFunctionService;
    @Autowired
    SyFuncOperatorService syFuncOperatorService;
    @Autowired
    SyUrlMappingService syUrlMappingService;
    @Autowired
    SyFuncOpUrlService syFuncOpUrlService;
    public FunctionController(){
        //mapQueryCols.put("name","name");
    }
    @ApiOperation(value = "获取所有功能")
    @RequiresPermissions("func:get")
    @GetMapping("/function")
    public RetResult getfunction() throws Exception {
        List<SyFunctionVo> funcTree = syFunctionService.getFuncTree();
        //Thread.sleep(8000);
        return RetResponse.makeRsp(funcTree);
    }

    @ApiOperation(value = "创建功能")
    @RequiresPermissions("func:create")
    @PostMapping("/function")
    public RetResult createFunc(@RequestBody SyFunctionVo syFuncVo) throws Exception {
        SyFunctionEntity syFunctionEntity =new SyFunctionEntity();
        VoConvertUtils.copyObjectProperties(syFuncVo,syFunctionEntity);
        syFunctionService.save(syFunctionEntity);
        return RetResponse.makeRsp("创建功能成功");
    }
    @ApiOperation(value = "编辑功能")
    @RequiresPermissions("func:edit")
    @PutMapping("/function/{funcId}")
    public RetResult editFunc(@PathVariable("funcId") Long funcId,@RequestBody SyFunctionVo syFuncVo) throws Exception {
        syFunctionService.updateFunc(funcId,syFuncVo);
        return RetResponse.makeRsp("修改功能成功");
    }
    @ApiOperation(value = "获取单个功能")
    @RequiresPermissions("func:get")
    @GetMapping("/function/{funcId}")
    public RetResult getFunc(@PathVariable("funcId") Long funcId) throws Exception {
        SyFunctionEntity syFunctionEntity = syFunctionService.getById(funcId);
        SyFunctionVo syFunctionVo =new SyFunctionVo();
        VoConvertUtils.copyObjectProperties(syFunctionEntity,syFunctionVo);
        return RetResponse.makeRsp(syFunctionVo);
    }
    @ApiOperation(value = "删除功能")
    @RequiresPermissions("func:del")
    @DeleteMapping("/function/{funcId}")
    public RetResult delFunc(@PathVariable("funcId") Long funcId) throws Exception {
        syFunctionService.deleteFunc(funcId);
        return RetResponse.makeRsp("删除功能成功");
    }

    @ApiOperation(value = "创建操作")
    @RequiresPermissions("funcOpr:create")
    @PostMapping("/function/opr")
    public RetResult createFuncOpr(@RequestBody SyFuncOperatorVo syFuncOprVo) throws Exception {
        SyFuncOperatorEntity syFuncOperatorEntity =new SyFuncOperatorEntity();
        VoConvertUtils.copyObjectProperties(syFuncOprVo,syFuncOperatorEntity);
        syFuncOperatorService.save(syFuncOperatorEntity);
        return RetResponse.makeRsp("创建操作成功");
    }
    @ApiOperation(value = "编辑操作")
    @RequiresPermissions("funcOpr:edit")
    @PutMapping("/function/opr/{funcOprId}")
    public RetResult editFuncOpr(@PathVariable("funcOprId") Long funcOprId,@RequestBody SyFuncOperatorVo syFuncOprVo) throws Exception {
        SyFuncOperatorEntity syFuncOperatorEntity =new SyFuncOperatorEntity();
        VoConvertUtils.copyObjectProperties(syFuncOprVo,syFuncOperatorEntity);
        syFuncOperatorService.updateById(syFuncOperatorEntity);
        return RetResponse.makeRsp("修改操作成功");
    }
    @ApiOperation(value = "删除操作")
    @RequiresPermissions("funcOpr:del")
    @DeleteMapping("/function/opr/{funcOprId}")
    public RetResult delFuncOpr(@PathVariable("funcOprId") Long funcOprId) throws Exception {
        syFuncOperatorService.removeById(funcOprId);
        return RetResponse.makeRsp("删除操作成功");
    }
    SyUrlMappingService.SyUrlMappingVoConvertUtils syUrlMappingVoConvertUtils=new SyUrlMappingService.SyUrlMappingVoConvertUtils();
    @ApiOperation(value = "urlmapping 添加")
    @RequiresPermissions("urlMaping:add")
    @PostMapping("/url-mapping")
    public RetResult addUrlMapping(@RequestBody SyUrlMappingVo syUrlMappingVo){
        syUrlMappingService.save(syUrlMappingVoConvertUtils.convertToT1(syUrlMappingVo));
        return RetResponse.makeRsp("添加成功");
    }
    @ApiOperation(value = "分页获取所有urlmapping")
    @RequiresPermissions("urlMaping:get")
    @GetMapping("/url-mapping")
    public RetResult getUrlMapping(@RequestParam("query") String query) {
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        adaptiveQueryColumn(pageQuery);
        Page<SyUrlMappingEntity> syUrlMappingEntityPage= selectPage(pageQuery,syUrlMappingService);
        List<SyUrlMappingVo> listVo=new ArrayList<>();
        for(SyUrlMappingEntity syUrlMappingEntity : syUrlMappingEntityPage.getRecords()){
            SyUrlMappingVo syUrlMappingVo=new SyUrlMappingVo();
            VoConvertUtils.copyObjectProperties(syUrlMappingEntity,syUrlMappingVo);
            listVo.add(syUrlMappingVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syUrlMappingEntityPage.getCurrent(), syUrlMappingEntityPage.getSize(), syUrlMappingEntityPage.getTotal(),listVo));
    }
    @ApiOperation(value = "获取某个操作对应的Uri")
    @RequiresPermissions(value = "funcOpUrl:get",note = "获取某个操作对应的Uri")
    @GetMapping("/function/opr/{funcOprId}/url")
    public RetResult getfunOpUrl(@PathVariable("funcOprId") Long funcOprId) {
        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("func_op_id",funcOprId);
        }};
        List<SyFuncOpUrlEntity> syFuncOpUrlEntityList = syFuncOpUrlService.list(queryWrapper);
        List<SyFuncOpUrlVo> syFuncOpUrlVoList=new ArrayList<>() ;
        for (SyFuncOpUrlEntity syFuncOpUrlEntity : syFuncOpUrlEntityList){
            SyFuncOpUrlVo syFuncOpUrlVo=new SyFuncOpUrlVo();
            VoConvertUtils.copyObjectProperties(syFuncOpUrlEntity,syFuncOpUrlVo);
            syFuncOpUrlVoList.add(syFuncOpUrlVo);
        }
        return RetResponse.makeRsp(syFuncOpUrlVoList);
    }
    @ApiOperation(value = "更新某个操作对应的Uri")
    @RequiresPermissions(value = "funcOpUrl:update",note = "更新某个操作对应的Uri")
    @PutMapping("/function/opr/{funcOprId}/url")
    public RetResult updatefunOpUrl(@PathVariable("funcOprId") Long funcOprId,@RequestBody List<SyFuncOpUrlVo> syFuncOpUrlVos) {
        QueryWrapper queryWrapper=new QueryWrapper(){{
            eq("func_op_id",funcOprId);
        }};
        syFuncOpUrlService.remove(queryWrapper);

        List<SyFuncOpUrlEntity> syFuncOpUrlEntityList=new ArrayList<>() ;
        for (SyFuncOpUrlVo syFuncOpUrlVo : syFuncOpUrlVos){
            SyFuncOpUrlEntity syFuncOpUrlEntity=new SyFuncOpUrlEntity();
            VoConvertUtils.copyObjectProperties(syFuncOpUrlVo,syFuncOpUrlEntity);
            syFuncOpUrlEntity.setId(null);
            syFuncOpUrlEntity.setFuncOpId(funcOprId);
            syFuncOpUrlEntityList.add(syFuncOpUrlEntity);
        }
        syFuncOpUrlService.saveBatch(syFuncOpUrlEntityList);
        return RetResponse.makeRsp("更新成功");
    }
}