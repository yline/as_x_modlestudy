package com.yline.runtime.xutils.lib.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * xUtils View注入
 * view注入接口
 *
 * @author fatenliyer
 * @date 2016-1-2
 */
public interface ViewInjector {

    /**
     * 注入view
     *
     * @param view view界面
     */
    void inject(View view);

    /**
     * 注入activity
     *
     * @param activity activity界面
     */
    void inject(android.app.Activity activity);

    /**
     * 注入fragment
     *
     * @param fragment  android.app.fragment、v4.fragment、androidx.fragment
     * @param inflater  inflate
     * @param container viewGroup
     * @return contentView对应的View
     */
    View inject(Object fragment, LayoutInflater inflater, ViewGroup container);

    /**
     * 注入任意一个对象
     *
     * @param object 注入的对象[持有View]
     * @param view   view内容
     */
    void inject(Object object, View view);

}












