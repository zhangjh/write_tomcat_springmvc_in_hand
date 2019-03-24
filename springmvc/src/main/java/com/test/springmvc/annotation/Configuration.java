package com.test.springmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : jihong.zjh@alibaba-inc.com
 * @date : 2019-03-21
 * Description: 配置类注解，基包路径和端口配置
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {
    String basePackage() ;

    int port() default 8080;
}
