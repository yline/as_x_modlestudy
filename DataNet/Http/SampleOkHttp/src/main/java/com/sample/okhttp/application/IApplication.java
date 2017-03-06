package com.sample.okhttp.application;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class IApplication extends BaseApplication
{
	public static final String TAG = "OkHttp";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}
}
