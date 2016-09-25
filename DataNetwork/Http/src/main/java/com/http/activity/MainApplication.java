package com.http.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "Http";

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath("Http");
		sdkConfig.setLogLocation(true);
		return sdkConfig;
	}
}
