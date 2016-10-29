package com.manager.activity;

import com.manager.NotifyManager;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/9/4.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "Manager";

	@Override
	public void onCreate()
	{
		super.onCreate();
		NotifyManager.setApplicationContext(this);
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
