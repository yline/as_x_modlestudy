package com.dm.gongchang.activity;

import android.os.Message;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "factory";
    
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
        appConfig.setFileLogPath("工厂模式");
        appConfig.setLogLocation(true);
        return appConfig;
    }
    
}
