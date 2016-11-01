package com.notify.activity;

import android.os.Bundle;
import android.view.View;

import com.notify.R;
import com.notify.Test.NotifyManagerUser;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private NotifyManagerUser notifyManagerUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
