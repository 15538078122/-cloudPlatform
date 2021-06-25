package com.hd.auservice.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @Author: liwei
 * @Description:
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;
    public static   ApplicationContext getApplicationContext(){
        return  applicationContext;
    }
    public static void setApplicationContext(ApplicationContext applicationContextParam) throws BeansException {
        SpringUtil.applicationContext = applicationContext=applicationContextParam;
    }
    static {
    }
    public static Object getObject(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }
    public static <T> T getObject(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String tClass) {
        return applicationContext.getBean(tClass);
    }

    public static <T> T getBean(String str,Class<T> tClass) {
        return (T)applicationContext.getBean(str);
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}
