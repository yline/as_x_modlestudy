package com.view.valueanimator.activity;

import android.os.Bundle;
import android.view.View;

import com.view.valueanimator.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_object_start).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_object_start");
				ObjectActivity.actionStart(MainActivity.this);
			}
		});
	}
}
