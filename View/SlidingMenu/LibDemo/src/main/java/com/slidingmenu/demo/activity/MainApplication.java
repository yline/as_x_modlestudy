package com.slidingmenu.demo.activity;

import android.os.Message;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "SlidingMenuLibDemo";

	@Override
	protected void handlerDefault(Message msg)
	{

	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig appConfig = new SDKConfig();
		appConfig.setLogFilePath("SlidingMenuLibDemo"); // 默认开启日志,并写到文件中
		return appConfig;
	}
}
