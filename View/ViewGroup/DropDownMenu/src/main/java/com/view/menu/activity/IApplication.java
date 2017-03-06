package com.view.menu.activity;


import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class IApplication extends BaseApplication
{
	public static final String TAG = "DropDownMenu";
	
	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
