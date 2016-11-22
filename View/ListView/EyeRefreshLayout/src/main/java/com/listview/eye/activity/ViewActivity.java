package com.listview.eye.activity;

import android.os.Bundle;
import android.view.View;

import com.listview.eye.R;
import com.listview.eye.view.EyeView;
import com.yline.base.BaseActivity;

public class ViewActivity extends BaseActivity
{
	private EyeView eyeView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);

		eyeView = (EyeView) findViewById(R.id.view_eye);

		findViewById(R.id.btn_view_start).setOnClickListener(new View.OnClickListener()
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
	}
}
