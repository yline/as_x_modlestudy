package com.yline.annotation.runtime.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Retention 保留的范围，单个
 * RetentionPolicy.SOURCE：源码
 * RetentionPolicy.CLASS：源码、字节码
 * RetentionPolicy.RUNTIME：源码、字节码、运行时
 * <
 * @Target 可以用来修饰哪些程序元素，多个。默认，全部可修饰
 * ElementType.TYPE,                接口、类、枚举、注解
 * ElementType.FIELD,               字段、枚举的常量
 * ElementType.METHOD,              方法
 * ElementType.PARAMETER,           方法参数
 * ElementType.CONSTRUCTOR,         构造函数
 * ElementType.LOCAL_VARIABLE,      局部变量
 * ElementType.ANNOTATION_TYPE,     注解
 * ElementType.PACKAGE,             包
 * ElementType.TYPE_PARAMETER,      接口、类、枚举、注解的参数
 * ElementType.TYPE_USE;            类型使用说明
 * <
 * @Inherited 添加该字段，说明该注解若作用在父类，子类也可享用到效果；若未添加，则享受不到
 * @Documented 是否会保存到 Javadoc 文档中，不添加代表false
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RuntimeAnnotation {

    String value() default "value";
}
