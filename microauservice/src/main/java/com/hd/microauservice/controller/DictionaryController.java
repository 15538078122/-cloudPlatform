package com.hd.microauservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.QueryExpression;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyDictItemVo;
import com.hd.common.vo.SyDictVo;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.entity.SyDictEntity;
import com.hd.microauservice.entity.SyDictItemEntity;
import com.hd.microauservice.service.SyDictItemService;
import com.hd.microauservice.service.SyDictService;
import com.hd.microauservice.utils.EnterpriseVerifyUtil;
import com.hd.microauservice.utils.VoConvertUtils;
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
@Api(tags = "字典Controller")
@RestController
@Slf4j
public class DictionaryController extends SuperQueryController {
    @Autowired
    SyDictService syDictService;
    @Autowired
    SyDictItemService syDictItemService;
    public DictionaryController(){
        mapQueryCols.put("sort","sort_");
        mapQueryCols.put("enterpriseId","enterprise_id");
    }
    @ApiOperation(value = "分页获取字典项")
    @RequiresPermissions("dictionary:list")
    @GetMapping("/dictionary")
    public RetResult getDictionary(String enterId,@RequestParam("query") String query){
        EnterpriseVerifyUtil.verifyEnterId(enterId);
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        QueryExpression queryExpression=new QueryExpression();
        queryExpression.setColumn("enterpriseId");
        queryExpression.setValue(enterId);
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);

        adaptiveQueryColumn(pageQuery);

        Page<SyDictEntity> syDictEntityPage = selectPage(pageQuery,syDictService);
        List<SyDictVo> listVo=new ArrayList<>();
        for(SyDictEntity syDictEntity : syDictEntityPage.getRecords()){
            SyDictVo syDictVo=new SyDictVo();
            VoConvertUtils.convertObject(syDictEntity,syDictVo);
            listVo.add(syDictVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syDictEntityPage.getCurrent(), syDictEntityPage.getSize(), syDictEntityPage.getTotal(),listVo));
    }
    @ApiOperation(value = "分页获取字典项的值")
    @RequiresPermissions("dictItem:get")
    @GetMapping("/dictionary/{id}/item")
    public RetResult getDictionaryItem(@PathVariable("id") Long dictId,@RequestParam("query") String query){
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        adaptiveQueryColumn(pageQuery);
        //添加字典项id
        QueryExpression queryExpression=new QueryExpression();
        queryExpression.setColumn("dict_id");
        queryExpression.setValue(dictId.toString());
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);
        Page<SyDictItemEntity> syDictItemEntityPage = selectPage(pageQuery,syDictItemService);
        List<SyDictItemVo> listVo=new ArrayList<>();
        for(SyDictItemEntity syDictItemEntity : syDictItemEntityPage.getRecords()){
            SyDictItemVo syDictItemVo=new SyDictItemVo();
            VoConvertUtils.convertObject(syDictItemEntity,syDictItemVo);
            listVo.add(syDictItemVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syDictItemEntityPage.getCurrent(), syDictItemEntityPage.getSize(), syDictItemEntityPage.getTotal(),listVo));
    }

    @ApiOperation(value = "创建字典项")
    @RequiresPermissions("dictionary:create")
    @PostMapping("/dictionary")
    public RetResult createDictionary(@RequestBody @Validated SyDictVo syDictVo) throws Exception {
        String enterId=syDictVo.getEnterpriseId();
        EnterpriseVerifyUtil.verifyEnterId(enterId);
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictEntity syDictEntity=new SyDictEntity();
        VoConvertUtils.convertObject(syDictVo.setEnterpriseId(enterId),syDictEntity);
        syDictService.save(syDictEntity);
        return RetResponse.makeRsp("创建字典项成功.");
    }
    @ApiOperation(value = "编辑字典项")
    @RequiresPermissions("dictionary:edit")
    @PutMapping("/dictionary")
    public RetResult editDictionary(@RequestBody @Validated SyDictVo syDictVo) throws Exception {
        String enterId=syDictVo.getEnterpriseId();
        EnterpriseVerifyUtil.verifyEnterId(enterId);
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictEntity syDictEntity=new SyDictEntity();
        VoConvertUtils.convertObject(syDictVo.setEnterpriseId(enterId),syDictEntity);
        syDictService.updateById(syDictEntity);
        return RetResponse.makeRsp("编辑字典项成功.");
    }
    @ApiOperation(value = "删除字典项")
    @RequiresPermissions("dictionary:delete")
    @DeleteMapping("/dictionary/{id}")
    public RetResult delDictionary(@PathVariable("id") Long id) throws Exception {
        syDictService.removeDict(id);
        return RetResponse.makeRsp("删除字典项成功.");
    }


    @ApiOperation(value = "创建字典项的值")
    @RequiresPermissions("dictItem:create")
    @PostMapping("/dictionary/item")
    public RetResult createDictionaryItem(@RequestBody @Validated SyDictItemVo syDictItemVo) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictItemEntity syDictEntity=new SyDictItemEntity();
        VoConvertUtils.convertObject(syDictItemVo,syDictEntity);
        syDictItemService.save(syDictEntity);
        return RetResponse.makeRsp("创建字典项的值成功.");
    }
    @ApiOperation(value = "编辑字典项的值")
    @RequiresPermissions("dictItem:edit")
    @PutMapping("/dictionary/item")
    public RetResult editDictionaryItem(@RequestBody @Validated SyDictItemVo syDictItemVo) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictItemEntity syDictEntity=new SyDictItemEntity();
        VoConvertUtils.convertObject(syDictItemVo,syDictEntity);
        syDictItemService.updateById(syDictEntity);
        return RetResponse.makeRsp("编辑字典项的值成功.");
    }
    @ApiOperation(value = "删除字典项的值")
    @RequiresPermissions("dictItem:delete")
    @DeleteMapping("/dictionary/item/{id}")
    public RetResult delDictionaryItem(@PathVariable("id") Long id) throws Exception {
        syDictItemService.removeById(id);
        return RetResponse.makeRsp("删除字典项的值成功.");
    }
}