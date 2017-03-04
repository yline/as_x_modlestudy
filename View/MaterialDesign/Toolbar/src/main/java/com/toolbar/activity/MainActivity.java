package com.toolbar.activity;

import android.os.Bundle;
import android.view.View;

import com.toolbar.R;
import com.toolbar.activity.style.IconBackActivity;
import com.toolbar.activity.style.TitleCenterActivity;
import com.yline.base.BaseAppCompatActivity;

/**
 * 入口 界面
 *
 * @author yline 2017/3/4 --> 10:23
 * @version 1.0.0
 */
public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_main_action_title_center).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TitleCenterActivity.actionStart(MainActivity.this);
			}
		});

		findViewById(R.id.btn_main_action_icon_back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				IconBackActivity.actionStart(MainActivity.this);
			}
		});
	}
}
