package com.hd.microservice.utils;
import com.hd.common.model.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
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
public class ApiUtilsTest implements ApplicationContextAware {
    public final static List<Api> API_LIST = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
         com.hd.common.utils.ApiUtils.ScanApplicationContext(applicationContext);
    }

}

