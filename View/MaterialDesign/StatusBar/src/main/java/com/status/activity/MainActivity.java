package com.status.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

/**
 * 参考：https://github.com/laobie/StatusBarUtil
 *
 * @author yline 2017/4/17 -- 13:04
 * @version 1.0.0
 */
public class MainActivity extends BaseAppCompatActivity
{
	private Toolbar mToolbar;

	private DrawerLayout mDrawerLayout;

	private ViewGroup contentLayout;

	private CheckBox mCheckBoxTranslucent;

	private TextView mTvStatusAlpha;

	private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), getResources().getColor(R.color.colorPrimary), StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.setDrawerListener(toggle);
		toggle.syncState();

		contentLayout = (ViewGroup) findViewById(R.id.main);
		mCheckBoxTranslucent = (CheckBox) findViewById(R.id.chb_translucent);
		mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);

		initViewClick();
	}

	private void initViewClick()
	{
		// 背景换图片
		mCheckBoxTranslucent.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mCheckBoxTranslucent.isChecked())
				{
					contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
					StatusBarUtil.setTranslucentForDrawerLayout(MainActivity.this, mDrawerLayout, mAlpha);
					mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				}
				else
				{
					contentLayout.setBackgroundDrawable(null);
					mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
					StatusBarUtil.setColorForDrawerLayout(MainActivity.this, mDrawerLayout, getResources().getColor(R.color.colorPrimary), mAlpha);
				}
			}
		});

		// 改变透明度
		SeekBar seekBar = (SeekBar) findViewById(R.id.sb_change_alpha);
		seekBar.setMax(255);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				mAlpha = progress;
				if (mCheckBoxTranslucent.isChecked())
				{
					StatusBarUtil.setTranslucentForDrawerLayout(MainActivity.this, mDrawerLayout, mAlpha);
				}
				else
				{
					StatusBarUtil.setColorForDrawerLayout(MainActivity.this, mDrawerLayout, getResources().getColor(R.color.colorPrimary), mAlpha);
				}
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
		seekBar.setProgress(StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);

		findViewById(R.id.btn_set_color).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ColorStatusBarActivity.actionStart(MainActivity.this);
			}
		});

		// 背景
		findViewById(R.id.btn_set_transparent).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ImageStatusBarActivity.actionStart(MainActivity.this, true);
			}
		});

		// 改变 透明度
		findViewById(R.id.btn_set_translucent).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ImageStatusBarActivity.actionStart(MainActivity.this, false);
			}
		});

		// 图片作为背景
		findViewById(R.id.btn_set_for_image_view).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ImageViewActivity.actionStart(MainActivity.this);
			}
		});

		// fragment
		findViewById(R.id.btn_use_in_fragment).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				UseInFragmentActivity.actionStart(MainActivity.this);
			}
		});

		// 简单 背景
		findViewById(R.id.btn_set_color_for_swipe_back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SwipeBackActivity.actionStart(MainActivity.this);
			}
		});
	}
}
