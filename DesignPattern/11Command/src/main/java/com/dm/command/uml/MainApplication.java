package com.dm.command.uml;

import android.os.Message;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "command_uml";

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
		sdkConfig.setLogFilePath("命令模式UML");
		sdkConfig.setLogLocation(true);
		return sdkConfig;
	}

}
