package com.lock.object.activity;

import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication
{
	public static final String TAG = "LockObject";

	public static final Object initLock = new Object();

	public static Object lock = new Object();

	@Override
	public void onCreate()
	{
		super.onCreate();
	}
}
