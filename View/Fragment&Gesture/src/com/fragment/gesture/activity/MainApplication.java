package com.fragment.gesture.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

import android.os.Message;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "Fragment&Gesture";
    
    @Override
    protected void handlerDefault(Message arg0)
    {
        
    }
    
    @Override
    protected SDKConfig initConfig()
    {
        SDKConfig appConfig = new SDKConfig();
        appConfig.setLogFilePath("Fragment&Gesture"); // 默认开启日志,并写到文件中
        return appConfig;
    }
}
