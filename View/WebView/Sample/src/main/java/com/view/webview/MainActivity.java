package com.view.webview;

import android.os.Bundle;
import android.view.View;

import com.view.webview.activity.BaiduUrlActivity;
import com.view.webview.activity.HokolUrlActivity;
import com.view.webview.activity.LocalUrlActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity
{

	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("Hokol Url", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				HokolUrlActivity.actionStart(MainActivity.this);
			}
		});

		addButton("Baidu Url", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				BaiduUrlActivity.actionStart(MainActivity.this);
			}
		});

		addButton("Local Html", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LocalUrlActivity.actionStart(MainActivity.this);
			}
		});
	}
}
