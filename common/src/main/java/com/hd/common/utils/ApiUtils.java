package com.hd.common.utils;
import com.alibaba.fastjson.JSON;
import com.hd.common.model.Api;
import com.hd.common.model.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
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
public class ApiUtils {
    public final static List<Api> API_LIST = new ArrayList<>();

    public static List<Api> ScanApplicationContext(ApplicationContext applicationContext,String servletContextPath) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RestController.class);
        //controler 上的@RefreshScope会造成加载了两个bean，进而scan重复
        beans.forEach((name, bean) -> {
            String className = bean.getClass().getName();
            className = className.split("\\$\\$")[0];
            try {
                Class<?> clazz = Class.forName(className);
//                获取根路径
                String[] rootPath = getClassApiPath(clazz);
                if (rootPath == null || rootPath.length==0) {
                    return;
                }
                for (Method method : clazz.getDeclaredMethods()) {
                    RequestPath methodPath = getApiPath(method);
                    if (methodPath == null) {
                        continue;
                    }
                    //                    api权限
                    RequiresPermissions permissions = method.getAnnotation(RequiresPermissions.class);
                    if(permissions==null || permissions.value().compareTo("")==0){
                        continue;
                    }
                    //   api路径
                    List<String> pathList = new ArrayList<>();
                    pathList.add(String.format("%s %s%s",methodPath.method,servletContextPath+rootPath[0],methodPath.path).replace("//","/"));
                    Api api = new Api();
                    api.setClassName(className)
                            .setMethodName(method.getName())
                            .setPath(pathList.get(0))
                            .setPermCode(permissions != null ? permissions.value() : "")
                            .setNote(permissions.note())
                    ;
                    API_LIST.add(api);
                    log.debug(JSON.toJSONString(api));
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return  API_LIST;
    }
    private static String[] getClassApiPath(AnnotatedElement annotatedElement) {
        RequestMapping a1 = annotatedElement.getAnnotation(RequestMapping.class);
        if (a1 != null) {
            return a1.value();
        }
        return new String[]{"/"};
    }
    private static RequestPath getApiPath(AnnotatedElement annotatedElement) {
        List<String> methods = new ArrayList<>();
        RequestMapping a1 = annotatedElement.getAnnotation(RequestMapping.class);
        PostMapping     a2 = annotatedElement.getAnnotation(PostMapping.class);
        GetMapping      a3 = annotatedElement.getAnnotation(GetMapping.class);
        PutMapping      a4 = annotatedElement.getAnnotation(PutMapping.class);
        DeleteMapping   a5 = annotatedElement.getAnnotation(DeleteMapping.class);

        String []paths2=new String[0];
        if (a1 != null) {
            methods.add("all");
            paths2= a1.value();
        }
        if (a2 != null) {
            methods.add("post");
            paths2= a2.value();
        }
        if (a3 != null) {
            methods.add("get");
            paths2= a3.value();
        }
        if (a4 != null) {
            methods.add("put");
            paths2= a4.value();
        }
        if (a5 != null) {
            methods.add("delete");
            paths2= a5.value();
        }
        //一般支取一个路径
        RequestPath[] paths = new RequestPath[0];
        if (paths2.length > 0) {
            paths = new RequestPath[1];
            paths[0]=new RequestPath();
            paths[0].method = methods.get(0);
            paths[0].path = paths2[0];
        }
        return paths.length==0?null:paths[0];
    }
    static class  RequestPath{
        String path;
        String method;
    }

}

