package com.yline.runtime.xutils.lib.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用条件：Fragment 或 Activity中
 *
 * @author yline 2020-03-15 -- 14:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
    /**
     * 绑定的内容
     */
    int value();
}
