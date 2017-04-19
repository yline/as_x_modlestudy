package com.nohttp.activity;

import android.os.Bundle;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;

public class MethodActivity extends CommonActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_method);

		String[] titles = getResources().getStringArray(R.array.activity_method_item);
		String[] titlesDes = getResources().getStringArray(R.array.activity_method_item_des);
	}
}
