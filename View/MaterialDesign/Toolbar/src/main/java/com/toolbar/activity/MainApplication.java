package com.toolbar.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "Toolbar";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
