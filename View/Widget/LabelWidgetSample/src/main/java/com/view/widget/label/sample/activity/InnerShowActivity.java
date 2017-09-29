package com.view.widget.label.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.yline.base.BaseAppCompatActivity;

public class InnerShowActivity extends BaseAppCompatActivity
{
	private static final String KeyInner = "layoutResId";

	public static void actionStart(Context context, @LayoutRes int layoutResId)
	{
		context.startActivity(new Intent(context, InnerShowActivity.class).putExtra(KeyInner, layoutResId));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int layoutRes = getIntent().getIntExtra(KeyInner, android.R.layout.activity_list_item);
		setContentView(layoutRes);
	}
}
