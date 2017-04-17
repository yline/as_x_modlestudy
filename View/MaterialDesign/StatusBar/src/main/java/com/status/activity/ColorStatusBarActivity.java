package com.status.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

import java.util.Random;

public class ColorStatusBarActivity extends BaseAppCompatActivity
{
	private Toolbar mToolbar;

	private SeekBar mSeekBar;

	private TextView mTvStatusAlpha;

	private int mColor;

	private int mAlpha;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_status_bar);

		mColor = getResources().getColor(R.color.colorPrimary);
		StatusBarUtil.setColor(this, mColor);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
		mSeekBar = (SeekBar) findViewById(R.id.sb_change_alpha);

		initViewClick();
	}

	private void initViewClick()
	{
		findViewById(R.id.btn_change_color).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Random random = new Random();
				mColor = 0xff000000 | random.nextInt(0xffffff);
				mToolbar.setBackgroundColor(mColor);
				StatusBarUtil.setColor(ColorStatusBarActivity.this, mColor, mAlpha);
			}
		});

		mSeekBar.setMax(255);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				mAlpha = progress;
				StatusBarUtil.setColor(ColorStatusBarActivity.this, mColor, mAlpha);
				mTvStatusAlpha.setText(String.valueOf(mAlpha));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}
		});
		mSeekBar.setProgress(StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
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
		context.startActivity(new Intent(context, ColorStatusBarActivity.class));
	}
}
