package com.sqlite.query.application;

import com.sqlite.query.helper.QueryDbManager;
import com.yline.application.BaseApplication;

public class IApplication extends BaseApplication
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		QueryDbManager.getInstance().init(this);
	}
}
