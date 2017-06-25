package com.okhttp.php.application;

import com.yline.application.BaseApplication;
import com.yline.http.XHttpConfig;

public class IApplication extends BaseApplication
{
	@Override
	public void onCreate()
	{
		super.onCreate();

		XHttpConfig.getInstance().init(this);
	}
}
