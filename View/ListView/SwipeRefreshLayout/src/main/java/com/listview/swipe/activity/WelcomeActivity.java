package com.listview.swipe.activity;

import android.os.Bundle;
import android.view.View;

import com.listview.swipe.R;
import com.yline.base.BaseAppCompatActivity;

public class WelcomeActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		findViewById(R.id.btn_welcome_sample).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SampleActivity.actionStart(WelcomeActivity.this);
			}
		});

		findViewById(R.id.btn_welcome_list).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ListViewActivity.actionStart(WelcomeActivity.this);
			}
		});

	}
}
