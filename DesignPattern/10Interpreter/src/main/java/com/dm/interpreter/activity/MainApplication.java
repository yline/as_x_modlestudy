package com.dm.interpreter.activity;

import android.os.Message;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "interpreter";

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	protected void handlerDefault(Message msg)
	{

	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath("解释器模式");
		sdkConfig.setLogLocation(true);
		return sdkConfig;
	}

}
