package com.manager.activity;

import android.os.Bundle;

import com.manager.R;
import com.manager.Test.APNManagerUser;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		APNManagerUser.test(MainActivity.this);
	}
}
