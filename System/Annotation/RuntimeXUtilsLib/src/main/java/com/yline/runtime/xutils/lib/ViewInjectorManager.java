package com.yline.runtime.xutils.lib;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.runtime.xutils.lib.utils.LogIoc;
import com.yline.runtime.xutils.lib.view.ViewFinderCompat;
import com.yline.runtime.xutils.lib.view.ViewInjector;
import com.yline.runtime.xutils.lib.view.contentview.ContentViewManager;
import com.yline.runtime.xutils.lib.view.event.EventManager;
import com.yline.runtime.xutils.lib.view.viewinject.ViewInjectManager;

public class ViewInjectorManager {
    private static ViewInjector instance;

    private ViewInjectorManager() {
    }

    public static ViewInjector getInstance() {
        if (null == instance) {
            synchronized (ViewInjector.class) {
                if (null == instance) {
                    instance = new ViewInjector() {

                        @Override
                        public void inject(View view) {
                            ViewFinderCompat compat = new ViewFinderCompat(view);

                            ViewInjectManager.init(view, compat);
                            EventManager.init(view, compat);
                        }

                        @Override
                        public void inject(Activity activity) {
                            ViewFinderCompat compat = new ViewFinderCompat(activity);

                            ContentViewManager.initActivity(activity);
                            ViewInjectManager.init(activity, compat);
                            EventManager.init(activity, compat);
                        }

                        @Override
                        public View inject(Object fragment, LayoutInflater inflater, ViewGroup container) {
                            View view = ContentViewManager.initFragment(fragment, inflater, container);
                            ViewFinderCompat compat = new ViewFinderCompat(view);

                            ViewInjectManager.init(fragment, compat);
                            EventManager.init(fragment, compat);
                            return view;
                        }

                        @Override
                        public void inject(Object object, View view) {
                            ViewFinderCompat compat = new ViewFinderCompat(view);

                            ViewInjectManager.init(object, compat);
                            EventManager.init(object, compat);
                        }
                    };
                }
            }
        }
        return instance;
    }
}


















