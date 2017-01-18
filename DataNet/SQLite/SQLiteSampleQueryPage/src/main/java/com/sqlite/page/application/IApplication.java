package com.sqlite.page.application;

import com.sqlite.page.helper.DbManager;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2017/1/18.
 */
public class IApplication extends BaseApplication
{
	public static final String TAG = "SQLiteSampleQueryPage";

	@Override
	public void onCreate()
	{
		super.onCreate();

		DbManager.getInstance().init(this);
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
