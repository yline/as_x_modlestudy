package com.gridview.threshold.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
    public static final String TAG = "GridView&ThresHold";
    
    @Override
    protected SDKConfig initConfig()
    {
        SDKConfig sdkConfig = new SDKConfig();
        sdkConfig.setLogFilePath(TAG);
        return sdkConfig;
    }
}
