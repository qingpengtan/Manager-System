package com.example.config;

import java.lang.annotation.*;

/**
 * 权限注解
 * Retention：保留至运行时，通过反射去获取注解信息。
 * Target：方法级别的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Permission {
    String value() default "";
}
