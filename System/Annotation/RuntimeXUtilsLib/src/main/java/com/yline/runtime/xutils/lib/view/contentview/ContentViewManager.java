package com.yline.runtime.xutils.lib.view.contentview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.runtime.xutils.lib.utils.CheckUtils;
import com.yline.runtime.xutils.lib.utils.LogIoc;
import com.yline.runtime.xutils.lib.view.annotation.ContentView;

import java.lang.reflect.Method;

/**
 * ContentView 的注解实现
 */
public class ContentViewManager {

    public static View initFragment(Object object, LayoutInflater inflater, ViewGroup container) {
        if (null == object || null == inflater || null == container) {
            return null;
        }

        Class<?> clazz = object.getClass();
        ContentView contentView = findContentView(clazz);
        if (null != contentView) {
            try {
                int viewId = contentView.value();
                if (viewId > 0) {
                    return inflater.inflate(viewId, container, false);
                }
            } catch (Throwable ex) {
                LogIoc.e(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public static void initActivity(Object object) {
        if (null == object) {
            return;
        }

        Class<?> clazz = object.getClass();
        ContentView contentView = findContentView(clazz);
        if (null != contentView) {
            try {
                int viewId = contentView.value();
                if (viewId > 0) {
                    Method method = clazz.getMethod("setContentView", int.class);
                    method.invoke(object, viewId);
                }
            } catch (Throwable ex) {
                LogIoc.e(ex.getMessage(), ex);
            }
        }
    }

    /**
     * 从父类获取注解View(子类找不到就从父类再找几次)
     *
     * @return ContentView
     */
    private static ContentView findContentView(Class<?> clazz) {
        // 参数为空
        if (CheckUtils.isIgnore(clazz)) {
            return null;
        }

        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (null == contentView) {
            return findContentView(clazz.getSuperclass());
        }
        return contentView;
    }
}
