package com.hd.microsysservice.conf.operlog;

import com.alibaba.fastjson.JSON;
import com.hd.common.utils.MongoLogUtil;
import com.hd.microsysservice.conf.RepeatedlyReadRequestWrapper;
import com.hd.microsysservice.conf.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class OperLogAspect {

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(OperLog)")
    public void operLogPoinCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     */
    @Pointcut("execution(* com.hd.microsysservice.controller..*.*(..))")
    public void operExceptionLogPoinCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            String operModul="";
            String operType="";
            String operDesc="";
            if (opLog != null) {
                operModul = opLog.operModul();
                operType = opLog.operType();
                operDesc = opLog.operDesc();
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            // 请求的参数
            String params="";
            String contentType = ((HttpServletRequest) request).getHeader("content-type");
            RepeatedlyReadRequestWrapper wrapper=null;
            if(contentType!=null&&contentType.compareTo("application/json")==0)
            {
                wrapper=new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
                params = new String(wrapper.getBody()) ;
            }
            else {
                Map<String, String> rtnMap = converMap(request.getParameterMap());
                // 将参数所在的数组转换成json
                params = JSON.toJSONString(rtnMap);
            }

            String logMsg =  String.format("\n操作模块:%s,操作类型:%s,操作描述:%s\n请求ip:%s,请求URI:%s,请求账号:%s\n请求方法:%s,请求参数:%s,返回结果:%s\n",
                    operModul,operType,operDesc
                    ,request.getRemoteHost(),request.getRequestURI(), SecurityContext.GetCurTokenInfo().getAccount()
                    ,methodName,params, JSON.toJSONString(keys));
            log.debug(logMsg);
            MongoLogUtil.logOp( operModul,operType,operDesc
                    ,request.getRemoteHost(),request.getRequestURI()
                    , SecurityContext.GetCurTokenInfo().getAccount(),params);
//            operlog.setOperRequParam(params); // 请求参数
//            operlog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
//            operlog.setOperUserId(UserShiroUtil.getCurrentUserLoginName()); // 请求用户ID
//            operlog.setOperUserName(UserShiroUtil.getCurrentUserName()); // 请求用户名称
//            operlog.setOperIp(IPUtil.getRemortIP(request)); // 请求IP
//            operlog.setOperUri(request.getRequestURI()); // 请求URI
//            operlog.setOperCreateTime(new Date()); // 创建时间
//            operlog.setOperVer(operVer); // 操作版本
//            operationLogService.insert(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            String operModul="";
            String operType="";
            String operDesc="";
            if (opLog != null) {
                operModul = opLog.operModul();
                operType = opLog.operType();
                operDesc = opLog.operDesc();
            }
            // 请求的参数
            String params="";
            String contentType = ((HttpServletRequest) request).getHeader("content-type");
            RepeatedlyReadRequestWrapper wrapper=null;
            if(contentType!=null&&contentType.compareTo("application/json")==0)
            {
                wrapper=new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
                params = new String(wrapper.getBody()) ;
            }
            else {
                Map<String, String> rtnMap = converMap(request.getParameterMap());
                // 将参数所在的数组转换成json
                params = JSON.toJSONString(rtnMap);
            }

            String errMsg =  String.format("\n请求ip:%s,请求URI:%s,请求账号:%s\n请求方法:%s,请求参数:%s,异常信息:%s\n"
                    ,request.getRemoteHost(),request.getRequestURI(), SecurityContext.GetCurTokenInfo().getAccount()
                    ,methodName,params,stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
            log.error(errMsg);
            MongoLogUtil.logErrorRequest(operModul,operType,operDesc
                    ,request.getRemoteHost(),request.getRequestURI()
                    , SecurityContext.GetCurTokenInfo().getAccount(),params,stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
//            excepLog.setExcRequParam(params); // 请求参数
//            excepLog.setOperMethod(methodName); // 请求方法名
//            excepLog.setExcName(e.getClass().getName()); // 异常名称
//            excepLog.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
//            excepLog.setOperUserId(UserShiroUtil.getCurrentUserLoginName()); // 操作员ID
//            excepLog.setOperUserName(UserShiroUtil.getCurrentUserName()); // 操作员名称
//            excepLog.setOperUri(request.getRequestURI()); // 操作URI
//            excepLog.setOperIp(IPUtil.getRemortIP(request)); // 操作员IP
//            excepLog.setOperVer(operVer); // 操作版本号
//            excepLog.setOperCreateTime(new Date()); // 发生异常时间


        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        //String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        String message = exceptionName + ":" + exceptionMessage ;
        return message;
    }
}
