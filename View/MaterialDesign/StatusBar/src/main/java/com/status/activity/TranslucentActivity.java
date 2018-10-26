package com.status.activity;

import android.app.Activity;
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

public class TranslucentActivity extends BaseAppCompatActivity {
	private static final String TRANSPARENT = "transparent";
	
	public static void launch(Context context, boolean isTransparent) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, TranslucentActivity.class);
			intent.putExtra(TRANSPARENT, isTransparent);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private RelativeLayout backgroundLayout;
	private TextView textView;
	private SeekBar seekBar;
	
	private boolean isBgChanged;
	private int mAlpha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translucent);
		
		initView();
		
		boolean isTransparent = getIntent().getBooleanExtra(TRANSPARENT, false);
		if (isTransparent) {
			StatusBarUtil.setTranslucent(this);
			seekBar.setVisibility(View.GONE);
		} else {
			StatusBarUtil.setTranslucent(this, 0x70);
			seekBar.setVisibility(View.VISIBLE);
			seekBar.setProgress(0x70);
		}
	}
	
	private void initView() {
		backgroundLayout = findViewById(R.id.translucent_root);
		textView = findViewById(R.id.translucent_alpha);
		seekBar = findViewById(R.id.translucent_seek_bar);
		
		initViewClick();
	}
	
	private void initViewClick() {
		findViewById(R.id.translucent_change).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isBgChanged = !isBgChanged;
				if (isBgChanged) {
					backgroundLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_girl));
				} else {
					backgroundLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
				}
			}
		});
		
		seekBar.setMax(0xff);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mAlpha = progress;
				StatusBarUtil.setTranslucent(TranslucentActivity.this, mAlpha);
				textView.setText(String.valueOf(mAlpha));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			
			}
		});
	}
}
