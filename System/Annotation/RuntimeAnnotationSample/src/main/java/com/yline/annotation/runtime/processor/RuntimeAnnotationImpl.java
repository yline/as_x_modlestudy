package com.yline.annotation.runtime.processor;

import androidx.annotation.NonNull;

import com.yline.utils.LogUtil;

import java.lang.reflect.Field;

public class RuntimeAnnotationImpl {
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
            RuntimeAnnotation fieldTag = field.getAnnotation(RuntimeAnnotation.class);
            if (null != fieldTag) {
                // 获取注解值
                String value = fieldTag.value();
                LogUtil.v("real value = " + value);

                // 将注解的值 放入进去
                field.setAccessible(true);

                try {
                    field.set(obj, value);
                } catch (IllegalArgumentException ex) {
                    // 放入值，异常情况也处理以下
                    Class fieldType = field.getType();
                    if (fieldType == int.class) {
                        field.set(obj, Integer.MAX_VALUE);
                    } else if (fieldType == float.class) {
                        field.set(obj, Float.MAX_VALUE);
                    } else if (fieldType == double.class) {
                        field.set(obj, Double.MAX_VALUE);
                    } else if (fieldType == long.class) {
                        field.set(obj, Long.MAX_VALUE);
                    } else {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
