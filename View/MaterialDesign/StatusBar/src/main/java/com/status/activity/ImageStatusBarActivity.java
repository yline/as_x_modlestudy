package com.status.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

public class ImageStatusBarActivity extends BaseAppCompatActivity
{
	public static final String EXTRA_IS_TRANSPARENT = "is_transparent";

	private TextView mTvStatusAlpha;

	private RelativeLayout mRootLayout;

	private boolean isBgChanged;

	private SeekBar mSbChangeAlpha;

	private boolean isTransparent;

	private int mAlpha;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_status_bar);

		mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);
		mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
		mSbChangeAlpha = (SeekBar) findViewById(R.id.sb_change_alpha);

		findViewById(R.id.btn_change_background).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				isBgChanged = !isBgChanged;
				if (isBgChanged)
				{
					mRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_girl));
				}
				else
				{
					mRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
				}
			}
		});

		isTransparent = getIntent().getBooleanExtra(EXTRA_IS_TRANSPARENT, false);
		if (isTransparent)
		{
			StatusBarUtil.setTransparent(this);
		}
		else
		{
			StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
		}

		if (!isTransparent)
		{
			mSbChangeAlpha.setVisibility(View.VISIBLE);
			setSeekBar();
		}
		else
		{
			mSbChangeAlpha.setVisibility(View.GONE);
		}
	}

	private void setSeekBar()
	{
		mSbChangeAlpha.setMax(255);
		mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				mAlpha = progress;
				StatusBarUtil.setTranslucent(ImageStatusBarActivity.this, mAlpha);
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

	public static void actionStart(Context context, boolean isTransparent)
	{
		Intent intent = new Intent(context, ImageStatusBarActivity.class);
		intent.putExtra(EXTRA_IS_TRANSPARENT, isTransparent);
		context.startActivity(intent);
	}
}
