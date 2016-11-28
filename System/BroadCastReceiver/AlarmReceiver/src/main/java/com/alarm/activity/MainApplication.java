package com.alarm.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * 只是简单的实现了一个闹钟提示,到Receiver结束
 * Created by yline on 2016/11/26.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "AlarmReceiver";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
