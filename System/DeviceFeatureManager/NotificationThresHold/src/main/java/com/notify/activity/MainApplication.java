package com.notify.activity;

import com.notify.NotifyManager;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/11/1.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "NotificationThresHold";

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
