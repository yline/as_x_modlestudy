package com.aidl.provider.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "AIDL&Provider";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath("AIDL&Provider"); // 默认开启日志,并写到文件中
		return sdkConfig;
	}
}
