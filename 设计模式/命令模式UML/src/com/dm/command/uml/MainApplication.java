package com.dm.command.uml;

import android.os.Message;

import com.yline.application.AppConfig;
import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "command_uml";
    
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
        appConfig.setFileLogPath("命令模式UML");
        appConfig.setLogLocation(true);
        return appConfig;
    }
    
}
