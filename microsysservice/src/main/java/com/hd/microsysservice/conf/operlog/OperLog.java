package com.hd.microsysservice.conf.operlog;

/**
 * @Author: liwei
 * @Description:
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志注解
 * @author wli
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) //注解在运行时可用
public @interface OperLog {
    String operModul() default ""; // 操作模块
    String operType() default "";  // 操作类型
    String operDesc() default "";  // 操作说明
}