package com.yline.runtime.xutils.lib.utils;

import java.util.HashSet;

public class CheckUtils {
    private static final HashSet<Class<?>> IGNORED = new HashSet<>();

    static {
        IGNORED.add(Object.class);
        IGNORED.add(android.app.Activity.class);
        IGNORED.add(android.app.Fragment.class);

        // v4 系列
        try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Exception ex) {

        }

        // x 系列
        try {
            IGNORED.add(Class.forName("androidx.fragment.app.Fragment"));
            IGNORED.add(Class.forName("androidx.fragment.app.FragmentActivity"));
        } catch (Exception ex) {

        }
    }

    /**
     * 判断clazz是否已经为结束标志
     *
     * @param clazz class参数
     * @return true - 「结束」
     */
    public static boolean isIgnore(Class<?> clazz) {
        return null == clazz || IGNORED.contains(clazz);
    }


}
