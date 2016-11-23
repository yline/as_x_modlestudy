package com.view.pattern.lock.activity;

import com.view.pattern.lock.view.LockPatternHelper;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "PatternLock";
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		LockPatternHelper.getInstance().setApplication(this);
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}