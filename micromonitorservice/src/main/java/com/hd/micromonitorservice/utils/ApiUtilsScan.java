package com.hd.micromonitorservice.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hd.common.RetResult;
import com.hd.common.model.Api;
import com.hd.common.utils.ApiUtils;
import com.hd.common.vo.SyUrlMappingVo;
import com.hd.micromonitorservice.service.MicroSysService;
import lombok.extern.slf4j.Slf4j;
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

    @Value("${config.scanUri}")
    boolean  scanUri;

    @Value("${server.servlet.context-path}")
    String servletContextPath;
    public  void  scanUri(MicroSysService microSysService) {
        if (scanUri) {
            List<Api> apis = ApiUtils.ScanApplicationContext(applicationContext, servletContextPath);
            QueryWrapper qw = new QueryWrapper();
            for (Api api : apis) {
                SyUrlMappingVo syUrlMappingVo = new SyUrlMappingVo();
                syUrlMappingVo.setUrl(api.getPath());
                syUrlMappingVo.setPermCode(api.getPermCode());
                syUrlMappingVo.setHandler(api.getClassName() + ":" + api.getMethodName());
                syUrlMappingVo.setNotes(api.getNote());
                //syUrlMappingService.save(syUrlMappingEntity);
                RetResult retResult = microSysService.addUrlMapping(syUrlMappingVo);
                //log.info(retResult.getMsg());
            }
        }
    }
    private  ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
}

