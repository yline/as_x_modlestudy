package com.system.service.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * @author yline 2016/11/9 --> 21:54
 * @version 1.0.0
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "ServiceSample";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
