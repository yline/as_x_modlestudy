package com.tabhost.viewpager.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "TabHost&ViewPager";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath("TabHost&ViewPager"); // 默认开启日志,并写到文件中
		return sdkConfig;
	}
}
