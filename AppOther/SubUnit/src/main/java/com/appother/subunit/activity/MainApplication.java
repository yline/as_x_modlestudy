package com.appother.subunit.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "AppOther&ProjectSubUnit";
    
    @Override
    protected SDKConfig initConfig()
    {
        SDKConfig sdkConfig = new SDKConfig();
        sdkConfig.setLogFilePath(TAG);
        return sdkConfig;
    }
}
