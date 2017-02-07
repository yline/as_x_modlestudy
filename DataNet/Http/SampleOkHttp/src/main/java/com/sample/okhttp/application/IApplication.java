package com.sample.okhttp.application;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2017/2/7.
 */
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
