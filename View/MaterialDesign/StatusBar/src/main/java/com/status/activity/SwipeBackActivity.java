package com.status.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

import java.util.Random;

public class SwipeBackActivity extends BaseAppCompatActivity
{
	private int mColor = Color.GRAY;

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_back);

		StatusBarUtil.setColorForSwipeBack(this, mColor, 38);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mToolbar.setBackgroundColor(mColor);

		findViewById(R.id.btn_change_color).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Random random = new Random();
				mColor = 0xff000000 | random.nextInt(0xffffff);
				mToolbar.setBackgroundColor(mColor);
				StatusBarUtil.setColorForSwipeBack(SwipeBackActivity.this, mColor, 38);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, SwipeBackActivity.class));
	}
}
