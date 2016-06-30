package com.lock.activity;

import java.io.File;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

import android.os.Message;

public class MainApplication extends BaseApplication
{
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
        appConfig.setFileLogPath("lockObject" + File.separator);
        appConfig.setLogLocation(true);
        return appConfig;
    }
    
}
