package com.sample.http.boye.activity;

import android.os.Bundle;

import com.yline.base.BaseFragmentActivity;

import org.xutils.x;

public class BaseIocFragmentActivity extends BaseFragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		x.view().inject(this);
	}
}
