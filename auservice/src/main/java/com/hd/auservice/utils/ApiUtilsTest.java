package com.hd.auservice.utils;
import com.alibaba.fastjson.JSON;
import com.hd.common.model.Api;
import com.hd.common.model.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

