package com.view.design.activity;

import android.os.Bundle;
import android.view.View;

import com.view.design.R;
import com.yline.base.BaseAppCompatActivity;

/**
 * 开启页面
 *
 * @author yline 2017/2/24 --> 15:52
 * @version 1.0.0
 */
public class WelcomeActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		findViewById(R.id.btn_action_viewpager).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ViewPagerActivity.actionStart(WelcomeActivity.this);
			}
		});

		findViewById(R.id.btn_action_replace).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NullActivity.actionStart(WelcomeActivity.this);
			}
		});
	}
}
