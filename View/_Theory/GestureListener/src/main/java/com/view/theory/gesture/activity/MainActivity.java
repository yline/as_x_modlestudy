package com.view.theory.gesture.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.view.theory.gesture.R;
import com.view.theory.gesture.helper.GestureHelper;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private GestureHelper gestureHelper;

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gestureHelper = new GestureHelper();
		imageView = (ImageView) findViewById(R.id.iv_test);

		// 会替换 之前的 Listener
		findViewById(R.id.btn_gesture_part).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_gesture_part");
				gestureHelper.testGesture(MainActivity.this, imageView);
			}
		});

		// 会替换 之前的 Listener
		findViewById(R.id.btn_gesture_simple).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_gesture_part");
				gestureHelper.testSimpleGesture(MainActivity.this, imageView);
			}
		});
	}
}
