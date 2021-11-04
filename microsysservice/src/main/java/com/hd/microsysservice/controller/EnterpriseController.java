package com.hd.microsysservice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hd.common.MyPage;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.controller.SuperQueryController;
import com.hd.common.model.RequiresPermissions;
import com.hd.common.vo.SyEnterpriseVo;
import com.hd.microsysservice.conf.operlog.OperLog;
import com.hd.microsysservice.entity.SyEnterpriseEntity;
import com.hd.microsysservice.service.SyEnterpriseService;
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
import java.util.HashMap;
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
    @PostMapping("/enterprise/list")
    public RetResult getEnterprise(@RequestParam("query") String query) {
        PageQueryExpressionList pageQuery = JSON.parseObject(query, PageQueryExpressionList.class);
        Assert.isTrue(pageQuery!=null,"查询参数错误!");
        adaptiveQueryColumn(pageQuery);
        //不查询逻辑删除的
//        QueryExpression queryExpression = new QueryExpression();
//        queryExpression.setColumn("delete_flag");
//        queryExpression.setValue("0");
//        queryExpression.setType("eq");
//        pageQuery.getQueryData().add(queryExpression);

        Page<SyEnterpriseEntity> syEnterpriseEntityPage = selectPage(pageQuery, syEnterpriseService);
        List<SyEnterpriseVo> listVo = new ArrayList<>();
        for (SyEnterpriseEntity syEnterpriseEntity : syEnterpriseEntityPage.getRecords()) {
            SyEnterpriseVo syEnterpriseVo = new SyEnterpriseVo();
            VoConvertUtils.copyObjectProperties(syEnterpriseEntity, syEnterpriseVo);
            listVo.add(syEnterpriseVo);
        }
        return RetResponse.makeRsp(new MyPage<>(syEnterpriseEntityPage.getCurrent(), syEnterpriseEntityPage.getSize(), syEnterpriseEntityPage.getTotal(), listVo));
    }

    @ApiOperation(value = "创建企业")
    @RequiresPermissions("enterprise:create")
    @PostMapping("/enterprise")
    @OperLog(operModul = "企业管理",operType = "创建",operDesc = "新建企业")
    public RetResult createEnterprise(@RequestBody @Validated SyEnterpriseVo syEnterpriseVo) throws Exception {
        SyEnterpriseEntity syEnterpriseEntity = new SyEnterpriseEntity();
        Boolean createRoles=false;
        if(syEnterpriseVo.getCreateRoles()!=null){
            createRoles=syEnterpriseVo.getCreateRoles()==1;
        }
        VoConvertUtils.copyObjectProperties(syEnterpriseVo, syEnterpriseEntity);
        syEnterpriseService.createEnterprise(syEnterpriseEntity,createRoles);
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
        VoConvertUtils.copyObjectProperties(syEnterpriseVo, syEnterpriseEntity);
        syEnterpriseService.updateById(syEnterpriseEntity);
        return RetResponse.makeRsp("编辑企业成功.");
    }

    @ApiOperation(value = "启用/停用企业")
    @RequiresPermissions("enterprise:enable")
    @PutMapping("/enterprise/enable")
    @OperLog(operModul = "企业管理",operType = "启用/停用",operDesc = "启用/停用企业")
    public RetResult enableEnterprise(@RequestParam("id") Long id,@RequestParam("enable") Boolean enable) throws Exception {
        if(!enable){
            syEnterpriseService.removeEnterpriseById(id);
            return RetResponse.makeRsp("停用企业成功.");
        }
        else{
            syEnterpriseService.recoverEnterprise(id);
            return RetResponse.makeRsp("启用企业成功.");
        }
    }

    @ApiOperation(value = "物理删除企业")
    @RequiresPermissions("enterprise:deletePhysically")
    @DeleteMapping("/enterprise/physically")
    public RetResult deleteEnterprisePhysically(String enterpriseId) throws Exception {
        Assert.notNull(enterpriseId,String.format("参数%s不存在!","enterpriseId"));
        VerifyUtil.verifyEnterId("root");
        List<SyEnterpriseEntity> syEnterpriseEntities = syEnterpriseService.listByMap(new HashMap<String, Object>() {{
            put("enterprise_id", enterpriseId);
        }});
        Assert.isTrue(syEnterpriseEntities.size()>0,String.format("企业%s不存在!",enterpriseId));
        syEnterpriseService.deleteEnterprisePhysically(syEnterpriseEntities.get(0).getId());
        return RetResponse.makeRsp("删除企业成功.");
    }
}
