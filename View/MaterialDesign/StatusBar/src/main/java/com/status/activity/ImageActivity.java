package com.status.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

public class ImageActivity extends BaseAppCompatActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, ImageActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private View mContentView;
	private TextView mAlphaTextView;
	
	private int mAlpha;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		
		initView();
		
		StatusBarUtil.setTranslucentForImageView(this, mContentView); // mContentView
	}
	
	private void initView() {
		mAlphaTextView = findViewById(R.id.image_alpha);
		mContentView = findViewById(R.id.image_offset);
		
		Toolbar toolbar = findViewById(R.id.image_toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		SeekBar seekBar = findViewById(R.id.image_seek_bar);
		seekBar.setMax(255);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAlpha = progress;
				StatusBarUtil.setTranslucentForImageView(ImageActivity.this, mContentView, mAlpha); // mContentView
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
