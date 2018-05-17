package com.recycler.snap;

import com.yline.application.BaseApplication;
import com.yline.view.fresco.common.FrescoConfig;

/**
 * 应用入口
 *
 * @author yline 2018/5/7 -- 13:36
 * @version 1.0.0
 */
public class IApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        FrescoConfig.initConfig(this, true);
    }
}
