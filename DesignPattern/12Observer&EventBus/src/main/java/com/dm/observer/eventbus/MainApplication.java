package com.dm.observer.eventbus;

import android.os.Message;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{

	public static final String TAG = "observer_EventBus";

	@Override
	protected void handlerDefault(Message msg)
	{

	}
	
	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath("观察者模式EventBus"); // 默认开启日志,并写到文件中
		return sdkConfig;
	}
}
