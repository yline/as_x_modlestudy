package com.yline.annotation.start.impl;

import com.yline.annotation.start.RuntimeFieldTag;
import com.yline.utils.LogUtil;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;

public class RuntimeFieldImpl {
    public static void init(Object obj) {
        if (null == obj) {
            return;
        }

        try {
            initInner(obj);
        } catch (Exception ex) {
            LogUtil.e("", ex);
        }
    }

    private static void initInner(@NonNull Object obj) throws IllegalAccessException {
        // 获取要解析的类
        Class clazz = obj.getClass();

        // 拿到所有Field
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取注解
            RuntimeFieldTag fieldTag = field.getAnnotation(RuntimeFieldTag.class);
            if (null != fieldTag) {
                // 获取注解值
                String value = fieldTag.value();
                LogUtil.v("real value = " + value);

                // 将注解的值 放入进去
                field.setAccessible(true);

                // 放入值
                Class fieldType = field.getType();
                if (fieldType == int.class) {
                    field.set(obj, 100);
                } else if (fieldType == long.class) {
                    field.set(obj, Long.MAX_VALUE);
                } else {
                    field.set(obj, value);
                }
            }
        }
    }


}
