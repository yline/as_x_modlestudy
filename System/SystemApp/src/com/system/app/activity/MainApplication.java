package com.system.app.activity;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

import android.os.Message;

public class MainApplication extends BaseApplication
{
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
        appConfig.setFileLogPath("SystemApp");
        appConfig.setLogLocation(true);
        return appConfig;
    }
}
