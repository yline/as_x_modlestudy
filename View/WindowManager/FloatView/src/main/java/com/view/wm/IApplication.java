package com.view.wm;

import com.yline.application.BaseApplication;

/**
 * App 入口
 *
 * @author yline 2018/1/11 -- 11:54
 * @version 1.0.0
 */
public class IApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        FloatService.launcher(this);
    }

    @Override
    public void onTerminate() {
        // 防止内存泄漏
        FloatService.unRegisterCircleClickListener();

        super.onTerminate();
    }
}
