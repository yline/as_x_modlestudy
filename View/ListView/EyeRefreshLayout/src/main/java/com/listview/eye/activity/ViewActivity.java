package com.listview.eye.activity;

import android.os.Bundle;
import android.view.View;

import com.listview.eye.R;
import com.listview.eye.view.EyeView;
import com.listview.eye.view.YProgressView;
import com.yline.base.BaseActivity;

public class ViewActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);

		final EyeView eyeView = (EyeView) findViewById(R.id.view_eye);

		findViewById(R.id.btn_view_eye).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				eyeView.startAnimate();
				eyeView.setProgress(1.0f);

				MainApplication.getHandler().postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						eyeView.stopAnimate();
					}
				}, 5000);
			}
		});

		final YProgressView yProgressView = (YProgressView) findViewById(R.id.view_process);

		findViewById(R.id.btn_view_process).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				yProgressView.startAnimate();
				yProgressView.setProgress(1.0f);

				MainApplication.getHandler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						yProgressView.stopAnimate();
					}
				}, 5000);
			}
		});

		findViewById(R.id.btn_start_listview).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MainActivity.actionStart(ViewActivity.this);
			}
		});
	}
}
