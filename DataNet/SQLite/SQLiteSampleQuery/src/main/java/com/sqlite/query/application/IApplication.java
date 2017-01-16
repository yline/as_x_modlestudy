package com.sqlite.query.application;

import com.sqlite.query.helper.QueryDbManager;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2017/1/16.
 */
public class IApplication extends BaseApplication
{
	public static final String TAG = "SQLiteSampleQuery";

	@Override
	public void onCreate()
	{
		super.onCreate();

		QueryDbManager.getInstance().init(this);
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
