package com.hd.microauservice.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.microauservice.conf.GeneralConfig;
import com.hd.microauservice.conf.SecurityContext;
import com.hd.microauservice.service.SyEnterpriseService;
import org.springframework.util.Assert;

/**
 * @Author: liwei
 * @Description:
 */
public class EnterpriseVerifyUtil {
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

}
