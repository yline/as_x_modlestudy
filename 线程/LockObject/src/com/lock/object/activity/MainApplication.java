package com.lock.object.activity;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

import android.os.Message;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "LockObject";
    
    public static final Object initLock = new Object();
    
    public static Object lock = new Object();
    
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    
    @Override
    protected void handlerDefault(Message msg)
    {
        
    }
    
    @Override
    protected AppConfig initConfig()
    {
        AppConfig appConfig = new AppConfig();
        appConfig.setFileLogPath("LockObject");
        appConfig.setLogLocation(true);
        return appConfig;
    }
    
}
