package com.sqlite.application;

import com.sqlite.Helper.MyDbManager;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2017/1/16.
 */
public class IApplication extends BaseApplication
{
	public static final String TAG = "SQLiteSample";

	@Override
	public void onCreate()
	{
		super.onCreate();

		// 初始化数据库对象
		MyDbManager.getInstance().init(this);
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
