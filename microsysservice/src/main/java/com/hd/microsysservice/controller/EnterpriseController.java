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
import com.hd.common.vo.SyEnterpriseVo;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.hd.microsysservice.service.SyEnterpriseService;
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
@Api(tags = "企业Controller")
@RestController
@Slf4j
public class EnterpriseController extends SuperQueryController {
    @Autowired
    SyEnterpriseService syEnterpriseService;

    public EnterpriseController() {
        mapQueryCols.put("name", "name");
    }

    @ApiOperation(value = "获取企业列表信息")
    @RequiresPermissions(value = "enterprise:list", note = "分页获取企业列表")
    @GetMapping("/enterprise")
    public RetResult getEnterprise(@RequestParam("query") String query) {
        PageQueryExpressionList pageQuery = JSON.parseObject(query, PageQueryExpressionList.class);
        adaptiveQueryColumn(pageQuery);
        //不查询逻辑删除的
        QueryExpression queryExpression = new QueryExpression();
        queryExpression.setColumn("delete_flag");
        queryExpression.setValue("0");
        queryExpression.setType("eq");
        pageQuery.getQueryData().add(queryExpression);
        Page<SyEnterpriseEntity> syEnterpriseEntityPage = selectPage(pageQuery, syEnterpriseService);
        List<SyEnterpriseVo> listVo = new ArrayList<>();
        for (SyEnterpriseEntity syEnterpriseEntity : syEnterpriseEntityPage.getRecords()) {
            SyEnterpriseVo syEnterpriseVo = new SyEnterpriseVo();
            VoConvertUtils.convertObject(syEnterpriseEntity, syEnterpriseVo);
            listVo.add(syEnterpriseVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syEnterpriseEntityPage.getCurrent(), syEnterpriseEntityPage.getSize(), syEnterpriseEntityPage.getTotal(), listVo));
    }

    @ApiOperation(value = "创建企业")
    @RequiresPermissions("enterprise:create")
    @PostMapping("/enterprise")
    public RetResult createEnterprise(@RequestBody @Validated SyEnterpriseVo syEnterpriseVo) {
        SyEnterpriseEntity syEnterpriseEntity = new SyEnterpriseEntity();
        VoConvertUtils.convertObject(syEnterpriseVo, syEnterpriseEntity);
        syEnterpriseService.createEnterprise(syEnterpriseEntity);
        return RetResponse.makeRsp("创建企业成功.");
    }

    @ApiOperation(value = "编辑企业")
    @RequiresPermissions("enterprise:edit")
    @PutMapping("/enterprise/{id}")
    public RetResult editEnterprise(@PathVariable("id") Long id, @RequestBody @Validated SyEnterpriseVo syEnterpriseVo) throws Exception {
        SyEnterpriseEntity syEnterpriseEntity = syEnterpriseService.getById(id);
        if (syEnterpriseEntity.getEnterpriseId().compareTo(syEnterpriseVo.getEnterpriseId()) != 0) {
            throw new Exception("企业编码不可以修改!");
        }
        syEnterpriseEntity = new SyEnterpriseEntity();
        VoConvertUtils.convertObject(syEnterpriseVo, syEnterpriseEntity);
        syEnterpriseService.updateById(syEnterpriseEntity);
        return RetResponse.makeRsp("编辑企业成功.");
    }

    @ApiOperation(value = "删除企业")
    @RequiresPermissions("enterprise:delete")
    @DeleteMapping("/enterprise/{id}")
    public RetResult deleteEnterprise(@PathVariable("id") Long id) {
        SyEnterpriseEntity syEnterpriseEntity = syEnterpriseService.getById(id);
        Assert.isTrue(syEnterpriseEntity!=null,String.format("企业Id:%s不存在!", id));
        Assert.isTrue(syEnterpriseEntity.getEnterpriseId().compareTo("root")!=0,String.format("不能删除特殊企业%s!","root"));
        syEnterpriseService.removeById(id);
        return RetResponse.makeRsp("删除企业成功.");
    }
}
