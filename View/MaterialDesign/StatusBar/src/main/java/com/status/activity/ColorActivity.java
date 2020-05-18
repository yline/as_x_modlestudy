package com.status.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

import java.util.Random;

public class ColorActivity extends BaseAppCompatActivity {
	public static void launch(Context context){
		if (null != context){
			Intent intent = new Intent();
			intent.setClass(context, ColorActivity.class);
			if (!(context instanceof Activity)){
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private Toolbar mToolbar;
	private SeekBar mSeekBar;
	private TextView mAlphaTextView;
	
	private int mColor;
	private int mAlpha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color);
		
		mColor = getResources().getColor(R.color.colorPrimary);
		
		initView();
		StatusBarUtil.setColor(this, mColor);
	}
	
	private void initView() {
		mToolbar = findViewById(R.id.color_toolbar);
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mAlphaTextView = findViewById(R.id.color_alpha);
		mSeekBar = findViewById(R.id.color_seek_bar);
		
		initViewClick();
	}
	
	private void initViewClick() {
		findViewById(R.id.color_change).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Random random = new Random();
				mColor = 0xff000000 | random.nextInt(0xffffff);
				mToolbar.setBackgroundColor(mColor);
				StatusBarUtil.setColor(ColorActivity.this, mColor, mAlpha);
			}
		});
		
		mSeekBar.setMax(0xff);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAlpha = progress;
				StatusBarUtil.setColor(ColorActivity.this, mColor, mAlpha);
				mAlphaTextView.setText(String.valueOf(mAlpha));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
		mSeekBar.setProgress(0x70);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
