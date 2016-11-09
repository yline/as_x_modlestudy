package com.system.service.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.system.service.R;
import com.system.service.TestService;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/11/9.
 */
public class ServiceActivity extends BaseAppCompatActivity implements ServiceConnection
{
	private static final String TAG = "ServiceActivity";

	private static final int BIND_SERVICE_CODE = 0;

	private TestService.TestBinder binder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);

		// 开启服务
		TestService.serviceStart(this);
		findViewById(R.id.btn_binder_execute).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "onClick btn_binder_execute binder = " + binder);
				if (null != binder)
				{
					binder.execute();
				}
			}
		});
	}

	@Override
	protected void onStart()
	{
		TestService.serviceBind(this, this, BIND_SERVICE_CODE);
		super.onStart();
	}

	/**
	 * 成功连接时,传入Service
	 * @param name
	 * @param service
	 */
	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		LogFileUtil.v(TAG, "onServiceConnected");
		binder = (TestService.TestBinder) service;
	}

	/**
	 * 连接意外丢失时调用;
	 * 而解除绑定或service崩溃或被强杀都不会调用
	 * @param name
	 */
	@Override
	public void onServiceDisconnected(ComponentName name)
	{
		LogFileUtil.v(TAG, "onServiceDisconnected");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		TestService.serviceUnbind(this, this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		TestService.serviceStop(this);
	}

	public static void actionStart(Context context)
	{
		Intent intent = new Intent();
		intent.setClass(context, ServiceActivity.class);
		context.startActivity(intent);
	}
}
