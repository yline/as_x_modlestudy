package com.view.menu.activity;


public class IApplication extends BaseApplication
{
	public static final String TAG = "DropDownMenu";
	
	@Override
	public SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		/*sdkConfig.setLogFilePath(TAG);*/
		return sdkConfig;
	}
}
