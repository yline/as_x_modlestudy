package com.yline.runtime.xutils.lib.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用条件：
 * 1，不注入静态字段
 * 2，不注入final字段
 * 3，不注入基本类型字段
 * 4，不注入数组类型字段
 *
 * @author yline 2020-03-15 -- 14:36
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    /**
     * 绑定的内容
     */
    int value();

    /**
     * parent view id
     */
    int parentId() default 0;
}
