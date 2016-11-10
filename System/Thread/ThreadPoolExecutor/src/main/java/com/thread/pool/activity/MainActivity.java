package com.thread.pool.activity;

import android.os.Bundle;
import android.view.View;

import com.thread.pool.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				for (int i = 0; i < 100000000; i++)
				{
					if (i % 10000000 == 0)
					{
						LogFileUtil.v("TestThread i = " + i);
					}
				}
			}
		};

		findViewById(R.id.btn_execute).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick btn_execute twice");
				// 调用两次就会执行两次,然后,点击Button两次,就会出现线程池的排队效果
				MainApplication.start(runnable, null);
				MainApplication.start(runnable, null);
			}
		});
	}


}
