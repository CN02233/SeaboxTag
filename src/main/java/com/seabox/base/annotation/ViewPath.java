package com.seabox.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 定义页面路径
 * 如页面路径为/jsp/test/testMain.jsp
 * 则使用注解@ViewPath("test"),之后controller方法返回testMain字符串
 * @author SongChaoqun
 *
 */
public  @interface ViewPath {
    String value()  default "";
}