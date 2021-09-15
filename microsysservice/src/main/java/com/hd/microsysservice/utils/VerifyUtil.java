package com.hd.microsysservice.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.PageQueryExpressionList;
import com.hd.common.model.QueryExpression;
import com.hd.microsysservice.conf.GeneralConfig;
import com.hd.microsysservice.conf.SecurityContext;
import com.hd.microsysservice.service.SyEnterpriseService;
import org.springframework.util.Assert;

/**
 * @Author: liwei
 * @Description:
 */
public class VerifyUtil {
    /**
     * 校验企业是否与当前企业一致，只有root企业可以操作所有企业
     * @param enterId
     */
    public  static  void  verifyEnterId(String enterId){
        String enterpriseId= SecurityContext.GetCurTokenInfo().getEnterpriseId();
        if(enterpriseId.compareTo(GeneralConfig.ROOT_ENTERPRISE_ID)!=0){
            Assert.isTrue(enterId.compareTo(enterpriseId)==0,"企业编码异常!");
        }else {
            //企业id验证是否存在
            SyEnterpriseService syEnterpriseService = (SyEnterpriseService)SpringContextUtil.getBean("syEnterpriseServiceImpl");
            //SpringContextUtil.getApplicationContext().getBeanDefinitionNames()
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("enterprise_id",enterId);
            Assert.isTrue(syEnterpriseService.getOne(queryWrapper)!=null,"企业编码异常!");
        }
    }
    /**
     * 校验pagequery 参数
     * @param enterId
     */
    public  static PageQueryExpressionList verifyQueryParam(String query,String column,String errMsg){
        Assert.isTrue(query!=null,"缺少查询参数query!");
        PageQueryExpressionList pageQuery= JSON.parseObject(query,PageQueryExpressionList.class);
        Assert.isTrue(pageQuery!=null,"查询参数错误!");
        if(column!=null){
            QueryExpression queryExpression = pageQuery.getQueryExpressionByColumn(column);
            Assert.isTrue(queryExpression!=null,errMsg);
            if(column.compareTo("enterpriseId")==0){
                String enterId=queryExpression.getValue();
                VerifyUtil.verifyEnterId(enterId);
            }
        }
        return pageQuery;
    }
}
