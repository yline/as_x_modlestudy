package com.manager.activity;

import android.os.Bundle;
import android.view.View;

import com.manager.R;
import com.manager.Test.APNManagerUser;
import com.manager.Test.NotifyManagerUser;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	private NotifyManagerUser notifyManagerUser;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_apn).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_apn");
				APNManagerUser.test(MainActivity.this);
			}
		});

		notifyManagerUser = new NotifyManagerUser();
		findViewById(R.id.btn_notify_build).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_notify_build");
				notifyManagerUser.testBuild(MainActivity.this);
			}
		});
		findViewById(R.id.btn_notify_buildin).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_notify_buildin");
				notifyManagerUser.testBuildIn(MainActivity.this);
			}
		});
		findViewById(R.id.btn_notify_cancel).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_notify_cancel");
				notifyManagerUser.testCancel();
			}
		});


	}
}
