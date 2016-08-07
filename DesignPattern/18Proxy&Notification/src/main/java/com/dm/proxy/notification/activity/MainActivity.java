package com.dm.proxy.notification.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.proxy.notification.R;
import com.dm.proxy.notification.proxy.NotifyProxy;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_notification).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> send notification");
				new NotifyProxy(MainActivity.this).send();
			}
		});
	}

}
