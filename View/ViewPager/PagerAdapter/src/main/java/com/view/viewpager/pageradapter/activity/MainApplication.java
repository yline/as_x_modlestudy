package com.view.viewpager.pageradapter.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/9/4.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "PagerAdapter";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath("PagerAdapter");
		return sdkConfig;
	}
}
