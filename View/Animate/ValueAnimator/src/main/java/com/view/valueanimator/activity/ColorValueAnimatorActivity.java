package com.view.valueanimator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.view.valueanimator.R;
import com.yline.base.BaseAppCompatActivity;

/**
 * Created by yline on 2016/10/2.
 */
public class ColorValueAnimatorActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_value_animator_color);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, ColorValueAnimatorActivity.class));
	}
}
