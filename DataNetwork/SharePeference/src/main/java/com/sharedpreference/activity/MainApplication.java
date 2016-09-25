package com.sharedpreference.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "SharePeference";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);  // 默认开启日志,并写到文件中
		sdkConfig.setLogLocation(true);
		return sdkConfig;
	}
}
