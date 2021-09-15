package com.hd.microsysservice.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.model.Api;
import com.hd.common.utils.ApiUtils;
import com.hd.microsysservice.entity.SyUrlMappingEntity;
import com.hd.microsysservice.service.SyUrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liwei
 * @Description: Api接口工具类
 */

@Slf4j
@Component
public class ApiUtilsScan implements ApplicationContextAware {
    public final static List<Api> API_LIST = new ArrayList<>();

    @Autowired
    SyUrlMappingService syUrlMappingService;

    @Value("${config.scanUri}")
    boolean  scanUri;

    @Value("${server.servlet.context-path}")
    String servletContextPath;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(scanUri){
            List<Api> apis = ApiUtils.ScanApplicationContext(applicationContext,servletContextPath);
            QueryWrapper qw = new QueryWrapper();
            syUrlMappingService.remove(null);
            for(Api api:apis){
                SyUrlMappingEntity syUrlMappingEntity=new SyUrlMappingEntity() ;
                syUrlMappingEntity.setUrl(api.getPath());
                syUrlMappingEntity.setPermCode(api.getPermCode());
                syUrlMappingEntity.setHandler(api.getClassName()+":"+api.getMethodName());
                syUrlMappingEntity.setNotes(api.getNote());
                syUrlMappingService.save(syUrlMappingEntity);
            }
        }
    }

}

