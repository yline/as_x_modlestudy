package com.yline.runtime.xutils.lib.view.viewinject;

import android.view.View;

import androidx.annotation.NonNull;

import com.yline.runtime.xutils.lib.utils.LogIoc;
import com.yline.runtime.xutils.lib.view.ViewFinderCompat;
import com.yline.runtime.xutils.lib.view.annotation.ViewInject;
import com.yline.runtime.xutils.lib.utils.CheckUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * ViewInject 实现
 */
public class ViewInjectManager {
    /**
     * ViewInject实现
     *
     * @param object 对象
     * @param compat view兼容类
     */
    public static void init(Object object, ViewFinderCompat compat) {
        if (null == object || null == compat) {
            return;
        }

        initDfs(object, object.getClass(), compat);
    }

    /**
     * ViewInject实现
     *
     * @param object 对象
     * @param clazz  对象的class
     * @param compat view兼容类
     */
    private static void initDfs(@NonNull Object object, Class<?> clazz, @NonNull ViewFinderCompat compat) {
        if (CheckUtils.isIgnore(clazz)) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                Class<?> fieldType = field.getType();

                // 不注入静态字段、不注入final字段
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                // 不注入基本类型字段、不注入数组类型字段
                if (fieldType.isPrimitive() || fieldType.isArray()) {
                    continue;
                }

                handleViewInject(field, object, clazz, compat);
            }
        }

        initDfs(object, clazz.getSuperclass(), compat);
    }

    private static void handleViewInject(Field field, Object object, Class<?> clazz, ViewFinderCompat compat) {
        ViewInject viewInject = field.getAnnotation(ViewInject.class);
        if (null != viewInject) {
            try {
                View view = compat.findViewById(viewInject.value(), viewInject.parentId());
                if (null != view) {
                    field.setAccessible(true);
                    field.set(object, view);
                } else {
                    throw new RuntimeException("Invalid id(" + viewInject.value() + ") for @ViewInject!" + clazz.getSimpleName());
                }
            } catch (Throwable ex) {
                LogIoc.e(ex.getMessage(), ex);
            }
        }
    }
}
