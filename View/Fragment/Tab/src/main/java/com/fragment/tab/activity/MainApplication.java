package com.fragment.tab.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "Fragment&Tab";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig appConfig = new SDKConfig();
		appConfig.setLogFilePath(TAG); // 默认开启日志,并写到文件中
		return appConfig;
	}
}
