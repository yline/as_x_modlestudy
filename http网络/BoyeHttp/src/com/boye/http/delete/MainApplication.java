package com.boye.http.delete;

import org.xutils.x;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

import android.os.Message;

/**
 * 继承之后,自己调用方法开启对应的功能
 */
public class MainApplication extends BaseApplication
{
    public static final String TAG = "BoyeHttp";
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
    
    @Override
    protected void handlerDefault(Message arg0)
    {
        
    }
    
    @Override
    protected SDKConfig initConfig()
    {
        SDKConfig appConfig = new SDKConfig();
        appConfig.setFileLogPath("BoyeHttp"); // 默认开启日志,并写到文件中
        return appConfig;
    }
}
