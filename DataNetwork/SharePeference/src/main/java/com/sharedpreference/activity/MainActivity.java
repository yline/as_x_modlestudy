package com.sharedpreference.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.data.network.sp.R;
import com.sharedpreference.utils.SPUtil;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_sp_normal).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_sp_normal");

				// 增加两条数据
				SharedPreferences sp = MainActivity.this.getSharedPreferences("mormalInfos", Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("username", "normalUsername");
				editor.putString("password", "normalPassword");
				LogFileUtil.v(MainApplication.TAG, "putString -> value - normalUsername");
				LogFileUtil.v(MainApplication.TAG, "putString -> value - normalUsername");
				editor.commit();

				// 更新两条数据
				editor.putString("username", "normalUpdateUsername");
				editor.putString("password", "normalUpdatePassword");
				LogFileUtil.v(MainApplication.TAG, "putString -> value - normalUpdateUsername");
				LogFileUtil.v(MainApplication.TAG, "putString -> value - normalUpdatePassword");
				editor.commit();

				// 删除一条数据
				editor.remove("password");
				LogFileUtil.v(MainApplication.TAG, "remove -> key - password");
				editor.commit();

				// 获取两条数据
				String username = sp.getString("username", "");
				String password = sp.getString("password", "");
				LogFileUtil.v(MainApplication.TAG, "getString -> key - username");
				LogFileUtil.v(MainApplication.TAG, "getString -> key - password");
				LogFileUtil.i(MainApplication.TAG, "usrname = " + username + ",password = " + password);
			}
		});

		findViewById(R.id.btn_sp_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_sp_util");

				// 增加两条数据
				SPUtil.put(MainActivity.this, "username", "utilUsername");
				SPUtil.put(MainActivity.this, "password", "utilPassword");
				LogFileUtil.v(MainApplication.TAG, "put -> value - utilUsername");
				LogFileUtil.v(MainApplication.TAG, "put -> value - utilPassword");

				// 更新两条数据
				SPUtil.put(MainActivity.this, "username", "utilUpdateUsername");
				SPUtil.put(MainActivity.this, "password", "utilUpdatePassword");
				LogFileUtil.v(MainApplication.TAG, "put -> value - utilUpdateUsername");
				LogFileUtil.v(MainApplication.TAG, "put -> value - utilUpdatePassword");

				// 删除一条数据
				SPUtil.remove(MainActivity.this, "password");
				LogFileUtil.v(MainApplication.TAG, "remove -> key - password");

				// 获取两条数据
				String username = (String) SPUtil.get(MainActivity.this, "username", "");
				String password = (String) SPUtil.get(MainActivity.this, "password", "");
				LogFileUtil.v(MainApplication.TAG, "get -> key - username");
				LogFileUtil.v(MainApplication.TAG, "get -> key - password");
				LogFileUtil.i(MainApplication.TAG, "usrname = " + username + ",password = " + password);
			}
		});
	}

}
