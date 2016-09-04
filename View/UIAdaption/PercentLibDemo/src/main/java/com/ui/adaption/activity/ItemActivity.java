package com.ui.adaption.activity;

import android.os.Bundle;

import com.ui.adaption.R;
import com.yline.base.BaseActivity;


public class ItemActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(getIntent().getIntExtra("contentId", R.layout.activity_item));

		setTitle(getIntent().getStringExtra("title"));
	}

}
