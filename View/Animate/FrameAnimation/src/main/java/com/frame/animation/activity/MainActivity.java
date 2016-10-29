package com.frame.animation.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.frame.animation.R;
import com.yline.log.LogFileUtil;

public class MainActivity extends AppCompatActivity
{
	private ImageView ivTest;

	private Button btnTest;

	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnTest = (Button) findViewById(R.id.btn_test);
		ivTest = (ImageView) findViewById(R.id.iv_test);

		ivTest.setBackgroundResource(R.drawable.yline_list);
		animationDrawable = (AnimationDrawable) ivTest.getBackground();

		btnTest.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btnTest");
				animationDrawable.start();
			}
		});
	}

}
