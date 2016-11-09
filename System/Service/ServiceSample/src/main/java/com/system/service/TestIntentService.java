package com.system.service;

import android.app.IntentService;
import android.content.Intent;

import com.yline.log.LogFileUtil;

/**
 * 并没有做测试;
 * IntentService是Service类的子类，用来处理异步请求,在onCreate()函数中
 * 通过HandlerThread单独开启一个线程来处理所有Intent请求对象,以免事务处理阻塞主线程
 * @author yline 2016/11/10 --> 1:58
 * @version 1.0.0
 */
public class TestIntentService extends IntentService
{
	private static final String TAG = TestIntentService.class.getSimpleName();

	public TestIntentService()
	{
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		LogFileUtil.v(TAG, "onHandleIntent");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		LogFileUtil.v(TAG, "onDestroy");
	}
}
