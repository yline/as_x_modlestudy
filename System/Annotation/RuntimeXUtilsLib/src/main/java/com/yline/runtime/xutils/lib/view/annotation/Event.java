package com.yline.runtime.xutils.lib.view.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用条件：
 * 1，静态方法不处理
 * 2，注解实现的方法，参数必须和对应的ListenerType保持一致。默认「View v」
 *
 * @author yline 2020-03-15 -- 14:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    /**
     * 控件的id集合, id小于1时不执行ui事件绑定.
     * 绑定的内容
     *
     * @return 一个个id
     */
    int[] value();

    /**
     * 控件的parent控件的id集合, 组合为(value[i], parentId[i] or 0).
     *
     * @return 一个个pid
     */
    int[] parentId() default 0;

    /**
     * 事件的setter方法名, 默认为set+type#simpleName.
     *
     * @return 设置点击事件的api名称
     */
    String setter() default "setOnClickListener";

    /**
     * 事件的listener, 默认为点击事件.
     *
     * @return 点击事件的class
     */
    Class<?> type() default View.OnClickListener.class;
}
