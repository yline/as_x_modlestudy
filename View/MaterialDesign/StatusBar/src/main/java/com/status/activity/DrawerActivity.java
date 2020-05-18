package com.status.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
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
public class DrawerActivity extends BaseAppCompatActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, DrawerActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private ViewGroup mMainLayout;
	private CheckBox mCheckBox;
	private TextView mAlphaTextView;
	
	private int mAlpha = 0x70;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		
		initView();
		StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout),
				getResources().getColor(R.color.colorPrimary), 0x70);
	}
	
	private void initView() {
		mToolbar = findViewById(R.id.drawer_toolbar);
		setSupportActionBar(mToolbar);
		mDrawerLayout = findViewById(R.id.drawer_layout);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.setDrawerListener(toggle);
		toggle.syncState();
		
		mMainLayout = findViewById(R.id.drawer_main);
		mCheckBox = findViewById(R.id.drawer_check);
		mAlphaTextView = findViewById(R.id.drawer_alpha);
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 背景换图片
		mCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCheckBox.isChecked()) {
					mMainLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
					StatusBarUtil.setTranslucentForDrawerLayout(DrawerActivity.this, mDrawerLayout, mAlpha);
					mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				} else {
					mMainLayout.setBackgroundDrawable(null);
					mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
					StatusBarUtil.setColorForDrawerLayout(DrawerActivity.this, mDrawerLayout, getResources().getColor(R.color.colorPrimary), mAlpha);
				}
			}
		});
		
		// 改变透明度
		SeekBar seekBar = findViewById(R.id.drawer_seek_bar);
		seekBar.setMax(0xff);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAlpha = progress;
				if (mCheckBox.isChecked()) {
					StatusBarUtil.setTranslucentForDrawerLayout(DrawerActivity.this, mDrawerLayout, mAlpha);
				} else {
					StatusBarUtil.setColorForDrawerLayout(DrawerActivity.this, mDrawerLayout, getResources().getColor(R.color.colorPrimary), mAlpha);
				}
				mAlphaTextView.setText(String.valueOf(mAlpha));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		seekBar.setProgress(0x70);
	}
}
