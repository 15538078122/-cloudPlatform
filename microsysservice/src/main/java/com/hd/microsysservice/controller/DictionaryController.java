package com.hd.microsysservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.model.TokenInfo;
import com.hd.common.vo.SyDictItemVo;
import com.hd.common.vo.SyDictVo;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.conf.operlog.OperLog;
import com.hd.microsysservice.entity.SyDictEntity;
import com.hd.microsysservice.entity.SyDictItemEntity;
import com.hd.microsysservice.service.SyDictItemService;
import com.hd.microsysservice.service.SyDictService;
import com.hd.microsysservice.utils.VerifyUtil;
import com.hd.microsysservice.utils.VoConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
        //mapQueryCols.put("enterpriseId","enterprise_id");
    }
    @ApiOperation(value = "分页获取字典项列表")
    @RequiresPermissions("dictionary:list")
    @PostMapping("/dictionary/list")
    public RetResult getDictionary(String enterId,@RequestParam(value = "query",required = false) String query){
        PageQueryExpressionList pageQuery=VerifyUtil.verifyQueryParam(query,"enterpriseId","enterpriseId不能为空!");
        adaptiveQueryColumn(pageQuery);

        Page<SyDictEntity> syDictEntityPage = selectPage(pageQuery,syDictService);
        List<SyDictVo> listVo=new ArrayList<>();
        for(SyDictEntity syDictEntity : syDictEntityPage.getRecords()){
            SyDictVo syDictVo=new SyDictVo();
            VoConvertUtils.copyObjectProperties(syDictEntity,syDictVo);
            listVo.add(syDictVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syDictEntityPage.getCurrent(), syDictEntityPage.getSize(), syDictEntityPage.getTotal(),listVo));
    }
    @ApiOperation(value = "分页获取字典项的值列表")
    @RequiresPermissions("dictItem:list")
    @PostMapping("/dictionary/item/list")
    public RetResult getDictionaryItem(@RequestParam("query") String query){
        PageQueryExpressionList pageQuery=VerifyUtil.verifyQueryParam(query,"dictId","dictId不能为空!");
        adaptiveQueryColumn(pageQuery);
        Long dictId=Long.parseLong(pageQuery.getQueryExpressionByColumn("dictId").getValue());
        VerifyUtil.verifyEnterId(syDictService.getById(dictId).getEnterpriseId());

        Page<SyDictItemEntity> syDictItemEntityPage = selectPage(pageQuery,syDictItemService);
        List<SyDictItemVo> listVo=new ArrayList<>();
        for(SyDictItemEntity syDictItemEntity : syDictItemEntityPage.getRecords()){
            SyDictItemVo syDictItemVo=new SyDictItemVo();
            VoConvertUtils.copyObjectProperties(syDictItemEntity,syDictItemVo);
            listVo.add(syDictItemVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syDictItemEntityPage.getCurrent(), syDictItemEntityPage.getSize(), syDictItemEntityPage.getTotal(),listVo));
    }

    @ApiOperation(value = "使用code分页获取字典项的值列表")
    @RequiresPermissions("dictItem:getbycode")
    @PostMapping("/dictionary/itembycode/list")
    public RetResult getDictionaryItemBycode(@RequestParam("query") String query) throws Exception {
        PageQueryExpressionList pageQuery=VerifyUtil.verifyQueryParam(query,"code","code不能为空!");
        adaptiveQueryColumn(pageQuery);
        VerifyUtil.verifyEnterId(pageQuery.getQueryExpressionByColumn("enterpriseId").getValue());

        MyPage<SyDictItemVo> syDictItemVoMyPage = syDictItemService.dictItembycode(pageQuery);
        return RetResponse.makeRsp(syDictItemVoMyPage);
//        QueryWrapper queryWrapper=new QueryWrapper(){{
//            eq("code",pageQuery.getQueryExpressionByColumn("code").getValue());
//            eq("enterprise_id",pageQuery.getQueryExpressionByColumn("enterpriseId").getValue());
//        }};
//        List<SyDictEntity> list = syDictService.list(queryWrapper);
//        Long dictId;
//        if(list!=null && list.size()>0){
//            dictId=list.get(0).getId();
//        }
//        else {
//            throw  new Exception("找不到字典项!");
//        }
//        pageQuery.getQueryExpressionByColumn("code").setColumn("dictId");
//        pageQuery.getQueryExpressionByColumn("dictId").setValue(dictId.toString());
//        pageQuery.getQueryData().remove(pageQuery.getQueryExpressionByColumn("enterpriseId"));
//
//        Page<SyDictItemEntity> syDictItemEntityPage = selectPage(pageQuery,syDictItemService);
//        List<SyDictItemVo> listVo=new ArrayList<>();
//        for(SyDictItemEntity syDictItemEntity : syDictItemEntityPage.getRecords()){
//            SyDictItemVo syDictItemVo=new SyDictItemVo();
//            VoConvertUtils.copyObjectProperties(syDictItemEntity,syDictItemVo);
//            listVo.add(syDictItemVo);
//        }
//        return RetResponse.makeRsp(new MyPage<>(syDictItemEntityPage.getCurrent(), syDictItemEntityPage.getSize(), syDictItemEntityPage.getTotal(),listVo));
    }

    @ApiOperation(value = "创建字典项")
    @RequiresPermissions("dictionary:create")
    @PostMapping("/dictionary")
    public RetResult createDictionary(@RequestBody @Validated SyDictVo syDictVo) throws Exception {
        String enterId=syDictVo.getEnterpriseId();
        VerifyUtil.verifyEnterId(enterId);
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictEntity syDictEntity=new SyDictEntity();
        VoConvertUtils.copyObjectProperties(syDictVo.setEnterpriseId(enterId),syDictEntity);
        syDictService.save(syDictEntity);
        return RetResponse.makeRsp("创建字典项成功.");
    }
    @ApiOperation(value = "编辑字典项")
    @RequiresPermissions("dictionary:edit")
    @PutMapping("/dictionary")
    public RetResult editDictionary(@RequestBody @Validated SyDictVo syDictVo) throws Exception {
        String enterId=syDictVo.getEnterpriseId();
        VerifyUtil.verifyEnterId(enterId);
        Long id=syDictVo.getId();
        SyDictEntity syDictEntity = syDictService.getById(id);
        Assert.notNull(syDictEntity,String.format("字典项%s不存在!",id));
        Assert.isTrue("shy_role".compareTo(syDictEntity.getCode())!=0
                        && "admin_role".compareTo(syDictEntity.getCode())!=0
                ,String.format("特殊字典项不可以修改!"));

        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        syDictEntity=new SyDictEntity();
        VoConvertUtils.copyObjectProperties(syDictVo.setEnterpriseId(enterId),syDictEntity);
        syDictService.updateById(syDictEntity);
        return RetResponse.makeRsp("编辑字典项成功.");
    }
    @ApiOperation(value = "删除字典项")
    @RequiresPermissions("dictionary:delete")
    @DeleteMapping("/dictionary/{id}")
    public RetResult delDictionary(@PathVariable("id") Long id) throws Exception {
        SyDictEntity syDictEntity = syDictService.getById(id);
        Assert.notNull(syDictEntity,String.format("字典项%s不存在!",id));
        Assert.isTrue("shy_role".compareTo(syDictEntity.getCode())!=0
                        && "admin_role".compareTo(syDictEntity.getCode())!=0
                        ,String.format("特殊字典项不可以删除!"));
        syDictService.removeDict(id);
        return RetResponse.makeRsp("删除字典项成功.");
    }

    @OperLog(operModul = "数据字典",operType = "创建",operDesc = "创建字典值")
    @ApiOperation(value = "创建字典项的值")
    @RequiresPermissions("dictItem:create")
    @PostMapping("/dictionary/item")
    public RetResult createDictionaryItem(@RequestBody @Validated SyDictItemVo syDictItemVo) throws Exception {
        Assert.notNull(syDictService.getById(syDictItemVo.getDictId()),"字典项不存在!");

        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictItemEntity syDictEntity=new SyDictItemEntity();
        VoConvertUtils.copyObjectProperties(syDictItemVo,syDictEntity);
        syDictItemService.save(syDictEntity);
        return RetResponse.makeRsp("创建字典项的值成功.");
    }
    @ApiOperation(value = "编辑字典项的值")
    @RequiresPermissions("dictItem:edit")
    @PutMapping("/dictionary/item")
    public RetResult editDictionaryItem(@RequestBody @Validated SyDictItemVo syDictItemVo) throws Exception {
        TokenInfo tokenInfo = SecurityContext.GetCurTokenInfo();
        SyDictItemEntity syDictEntity=new SyDictItemEntity();
        VoConvertUtils.copyObjectProperties(syDictItemVo,syDictEntity);
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