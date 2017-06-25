package com.okhttp.php.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.okhttp.php.http.XHttpUtils;
import com.yline.http.XHttpAdapter;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity
{
	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("测试80端口", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				XHttpUtils.doPhpTest80(new XHttpAdapter<String>()
				{
					@Override
					public void onSuccess(String s)
					{
						new AlertDialog.Builder(MainActivity.this).setMessage(s).show();
					}
				});
			}
		});

		addButton("测试8080端口", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				XHttpUtils.doPhpTest8080(new XHttpAdapter<String>()
				{
					@Override
					public void onSuccess(String s)
					{
						new AlertDialog.Builder(MainActivity.this).setMessage(s).show();
					}
				});
			}
		});
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, MainActivity.class));
	}
}
