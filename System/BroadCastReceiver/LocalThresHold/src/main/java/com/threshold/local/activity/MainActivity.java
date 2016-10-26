package com.threshold.local.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.threshold.local.R;
import com.threshold.local.receiver.TestReceiver;
import com.yline.base.BaseFragmentActivity;
import com.yline.log.LogFileUtil;


/**
 * 本地广播特点：
 * 1,只能动态注册
 * 2,无法接收其它广播的消息		(单通道)
 * 3,无法发送给其他广播消息		(单通道)
 * @author yline 2016/10/26 --> 7:56
 * @version 1.0.0
 */
public class MainActivity extends BaseFragmentActivity
{
	private TestReceiver mLocalReceiver = new TestReceiver();

	// 管理器
	private LocalBroadcastManager mLocalBroadcastManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		registerReceiver(MainActivity.this);

		findViewById(R.id.btn_send_local_one).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_send_local_one");

				mLocalBroadcastManager.sendBroadcast(new Intent(TestReceiver.ACTION_ONE));
			}
		});
		findViewById(R.id.btn_send_local_two).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_send_local_two");

				mLocalBroadcastManager.sendBroadcast(new Intent(TestReceiver.ACTION_TWO));
			}
		});
	}

	private void registerReceiver(Context context)
	{
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TestReceiver.ACTION_ONE);
		intentFilter.addAction(TestReceiver.ACTION_TWO);

		mLocalBroadcastManager.registerReceiver(mLocalReceiver, intentFilter);
	}

	private void unRegisterReceiver()
	{
		mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unRegisterReceiver();
	}
}
