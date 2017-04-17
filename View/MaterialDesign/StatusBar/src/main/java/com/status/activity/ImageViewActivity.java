package com.status.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

public class ImageViewActivity extends BaseAppCompatActivity
{
	private Toolbar mToolbar;
	
	private View mViewNeedOffset;
	
	private SeekBar mSbChangeAlpha;
	
	private TextView mTvStatusAlpha;
	
	private int mAlpha;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mViewNeedOffset = findViewById(R.id.view_need_offset);
		StatusBarUtil.setTranslucentForImageView(this, mViewNeedOffset);
		
		mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
		mSbChangeAlpha = (SeekBar) findViewById(R.id.sb_change_alpha);
		
		mSbChangeAlpha.setMax(255);
		mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				mAlpha = progress;
				StatusBarUtil.setTranslucentForImageView(ImageViewActivity.this, mAlpha, mViewNeedOffset);
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
		mSbChangeAlpha.setProgress(StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
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
		context.startActivity(new Intent(context, ImageViewActivity.class));
	}
}
