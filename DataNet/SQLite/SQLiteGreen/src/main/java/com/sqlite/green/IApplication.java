package com.sqlite.green;

import android.app.Application;

public class IApplication extends Application {

    private static IApplication iApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        iApplication = this;
    }

    public static IApplication getApplication() {
        return iApplication;
    }
}
