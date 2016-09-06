package com.viewpager.indicatorhelper.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/9/6.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "ViewPager&IndicatorHelper";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
