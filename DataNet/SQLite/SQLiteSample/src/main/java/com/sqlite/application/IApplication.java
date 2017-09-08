package com.sqlite.application;

import com.sqlite.Helper.ApiDbManager;
import com.sqlite.Helper.MyDbManager;
import com.yline.application.BaseApplication;

public class IApplication extends BaseApplication
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		// 初始化,语句形式执行的,数据库对象
		MyDbManager.getInstance().init(this);

		// 初始化,api形式可执行的,数据库对象
		ApiDbManager.getInstance().init(this);
	}

}
