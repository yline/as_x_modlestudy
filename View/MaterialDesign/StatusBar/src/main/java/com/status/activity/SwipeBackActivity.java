package com.status.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.status.R;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

import java.util.Random;

public class SwipeBackActivity extends BaseAppCompatActivity {
	public static void launch(Context context){
		if (null != context){
			Intent intent = new Intent();
			intent.setClass(context, SwipeBackActivity.class);
			if (!(context instanceof Activity)){
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private int mColor = Color.GRAY;
	
	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_back);
		
		initView();
		
		StatusBarUtil.setColorForCoordinatorLayout(this, mColor, 0xdd);
	}
	
	private void initView() {
		mToolbar = findViewById(R.id.image_toolbar);
		setSupportActionBar(mToolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mToolbar.setBackgroundColor(mColor);
		
		findViewById(R.id.btn_change_color).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Random random = new Random();
				mColor = 0xff000000 | random.nextInt(0xffffff);
				mToolbar.setBackgroundColor(mColor);
				StatusBarUtil.setColorForCoordinatorLayout(SwipeBackActivity.this, mColor, 0xdd);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
