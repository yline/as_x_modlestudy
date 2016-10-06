package com.view.theory.gesture.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/10/6.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "GestureListener";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
