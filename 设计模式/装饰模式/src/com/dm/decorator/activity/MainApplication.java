package com.dm.decorator.activity;

import android.os.Message;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "decorator";
    
    @Override
    protected void handlerDefault(Message msg)
    {
        
    }
    
    @Override
    protected AppConfig initConfig()
    {
        AppConfig appConfig = new AppConfig();
        appConfig.setFileLogPath("装饰模式"); // 默认开启日志,并写到文件中
        return appConfig;
    }
}
