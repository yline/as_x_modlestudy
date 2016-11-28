package com.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;

import com.alarm.R;
import com.yline.base.BaseActivity;

/**
 * @author yline 2016/11/26 --> 12:33
 * @version 1.0.0
 */
public class MainActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 自定义闹钟
		findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				CustomActivity.actionStart(MainActivity.this);
			}
		});
		
		// 调用系统闹钟
		findViewById(R.id.btn_system).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent alarmsIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
				startActivity(alarmsIntent);
			}
		});
	}
	
}
