package com.easybbs.annotation;

import java.lang.annotation.*;

/**
 * @author: iohw
 * @date: 2024/5/13 19:10
 * @description: aop拦截
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GlobalInterceptor {
    /**
     * 是否需要登录
     * @return
     */
    boolean checkLogin() default false;

    /**
     * 是否需要校验参数
     * @return
     */
    boolean checkParams() default false;

}
