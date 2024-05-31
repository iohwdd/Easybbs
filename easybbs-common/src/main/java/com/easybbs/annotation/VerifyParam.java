package com.easybbs.annotation;

import com.easybbs.entity.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: iohw
 * @date: 2024/5/13 19:35
 * @description: 校验项目
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParam {
    boolean required() default false;
    int max() default -1;
    int min() default -1;
    /**
     * 正则校验 默认正则表达式为空
     */
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;
}
