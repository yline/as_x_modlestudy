package com.sqlite.page.application;

import com.sqlite.page.helper.DbManager;
import com.yline.application.BaseApplication;

public class IApplication extends BaseApplication
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		DbManager.getInstance().init(this);
	}
}
