package com.boye.http.delete;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

import org.xutils.x;

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
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		sdkConfig.setLogLocation(true);
		return sdkConfig;
	}
}
