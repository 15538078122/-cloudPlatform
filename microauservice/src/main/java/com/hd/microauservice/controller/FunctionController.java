package com.hd.microauservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.*;
import com.hd.microauservice.entity.SyFuncOpUrlEntity;
import com.hd.microauservice.entity.SyFuncOperatorEntity;
import com.hd.microauservice.entity.SyFunctionEntity;
import com.hd.microauservice.entity.SyUrlMappingEntity;
import com.hd.microauservice.service.*;
import com.hd.microauservice.utils.VoConvertUtils;
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
public class FunctionController {

    @Autowired
    SyFunctionService  syFunctionService;
    @Autowired
    SyFuncOperatorService syFuncOperatorService;
    @Autowired
    SyUrlMappingService syUrlMappingService;
    @Autowired
    SyFuncOpUrlService syFuncOpUrlService;

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
        VoConvertUtils.convertObject(syFuncVo,syFunctionEntity);
        syFunctionService.save(syFunctionEntity);
        return RetResponse.makeRsp("创建菜单成功");
    }
    @ApiOperation(value = "编辑功能")
    @RequiresPermissions("func:edit")
    @PutMapping("/function/{funcId}")
    public RetResult editFunc(@PathVariable("funcId") Long funcId,@RequestBody SyFunctionVo syFuncVo) throws Exception {
        SyFunctionEntity syFunctionEntity =new SyFunctionEntity();
        VoConvertUtils.convertObject(syFuncVo,syFunctionEntity);
        syFunctionService.updateById(syFunctionEntity);
        return RetResponse.makeRsp("修改菜单成功");
    }
    @ApiOperation(value = "获取单个功能")
    @RequiresPermissions("func:get")
    @GetMapping("/function/{funcId}")
    public RetResult getFunc(@PathVariable("funcId") Long funcId) throws Exception {
        SyFunctionEntity syFunctionEntity = syFunctionService.getById(funcId);
        SyFunctionVo syFunctionVo =new SyFunctionVo();
        VoConvertUtils.convertObject(syFunctionEntity,syFunctionVo);
        return RetResponse.makeRsp(syFunctionVo);
    }
    @ApiOperation(value = "删除功能")
    @RequiresPermissions("func:del")
    @DeleteMapping("/function/{funcId}")
    public RetResult delFunc(@PathVariable("funcId") Long funcId) throws Exception {
        syFunctionService.removeById(funcId);
        return RetResponse.makeRsp("删除功能成功");
    }

    @ApiOperation(value = "创建操作")
    @RequiresPermissions("funcOpr:create")
    @PostMapping("/function/opr")
    public RetResult createFuncOpr(@RequestBody SyFuncOperatorVo syFuncOprVo) throws Exception {
        SyFuncOperatorEntity syFuncOperatorEntity =new SyFuncOperatorEntity();
        VoConvertUtils.convertObject(syFuncOprVo,syFuncOperatorEntity);
        syFuncOperatorService.save(syFuncOperatorEntity);
        return RetResponse.makeRsp("创建操作成功");
    }
    @ApiOperation(value = "编辑操作")
    @RequiresPermissions("funcOpr:edit")
    @PutMapping("/function/opr/{funcOprId}")
    public RetResult editFuncOpr(@PathVariable("funcOprId") Long funcOprId,@RequestBody SyFuncOperatorVo syFuncOprVo) throws Exception {
        SyFuncOperatorEntity syFuncOperatorEntity =new SyFuncOperatorEntity();
        VoConvertUtils.convertObject(syFuncOprVo,syFuncOperatorEntity);
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
    @ApiOperation(value = "获取所有urlmapping")
    @RequiresPermissions("urlMaping:get")
    @GetMapping("/url-mapping")
    public RetResult getUrlMapping(String url,String note) {
        QueryWrapper queryWrapper=new QueryWrapper(){{
            if(url!=null) {
                like("url",url);
            }
            if(note!=null) {
                like("notes",note);
            }
        }};
        List<SyUrlMappingEntity> syUrlMappingEntityList = syUrlMappingService.list(queryWrapper);
        List<SyUrlMappingVo> syUrlMappingVos=new ArrayList<>() ;
        for (SyUrlMappingEntity syUrlMappingEntity : syUrlMappingEntityList){
            SyUrlMappingVo syUrlMappingVo=new SyUrlMappingVo();
            VoConvertUtils.convertObject(syUrlMappingEntity,syUrlMappingVo);
            syUrlMappingVos.add(syUrlMappingVo);
        }
        return RetResponse.makeRsp(syUrlMappingVos);
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
            VoConvertUtils.convertObject(syFuncOpUrlEntity,syFuncOpUrlVo);
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
            VoConvertUtils.convertObject(syFuncOpUrlVo,syFuncOpUrlEntity);
            syFuncOpUrlEntityList.add(syFuncOpUrlEntity);
        }
        syFuncOpUrlService.saveBatch(syFuncOpUrlEntityList);
        return RetResponse.makeRsp("更新成功");
    }
}