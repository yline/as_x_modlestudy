package com.listview.swipe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.listview.swipe.R;
import com.listview.swipe.viewhelper.SampleHelper;
import com.yline.base.BaseAppCompatActivity;

public class SampleActivity extends BaseAppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);

		SampleHelper sampleHelper = new SampleHelper();
		sampleHelper.init((SwipeRefreshLayout) findViewById(R.id.swipe_container));
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SampleActivity.class));
	}
}
