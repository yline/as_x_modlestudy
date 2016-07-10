package com.json.activity;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

import android.os.Message;

public class MainApplication extends BaseApplication
{
    
    @Override
    protected void handlerDefault(Message msg)
    {
        
    }
    
    @Override
    protected AppConfig initConfig()
    {
        AppConfig appConfig = new AppConfig();
        appConfig.setFileLogPath("Json"); // 默认开启日志,并写到文件中
        return appConfig;
    }
}
