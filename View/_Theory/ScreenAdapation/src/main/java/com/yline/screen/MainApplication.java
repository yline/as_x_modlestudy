package com.yline.screen;

import android.app.Application;

public class MainApplication extends Application {
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Application getApplication() {
        return mApplication;
    }
}
