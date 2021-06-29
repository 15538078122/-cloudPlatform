package com.hd.common.model;

import java.lang.annotation.*;

/**
 * @Author: liwei
 * @Description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {
    String value() default "";
}
