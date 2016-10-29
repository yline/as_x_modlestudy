package com.view.animate.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.view.animate.R;
import com.view.animate.helper.AlphaAnimationHelper;
import com.view.animate.helper.AnimationSetHelper;
import com.view.animate.helper.RotateAnimationHelper;
import com.view.animate.helper.ScaleAnimationHelper;
import com.view.animate.helper.TranslateAnimationHelper;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private ImageView ivTest;

	private AnimationSetHelper animationSetHelper;

	private AlphaAnimationHelper alphaAnimationHelper;

	private RotateAnimationHelper rotateAnimationHelper;

	private ScaleAnimationHelper scaleAnimationHelper;

	private TranslateAnimationHelper translateAnimationHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initData()
	{
		animationSetHelper = new AnimationSetHelper();
		alphaAnimationHelper = new AlphaAnimationHelper();
		rotateAnimationHelper = new RotateAnimationHelper();
		scaleAnimationHelper = new ScaleAnimationHelper();
		translateAnimationHelper = new TranslateAnimationHelper();
	}

	private void initView()
	{
		ivTest = (ImageView) findViewById(R.id.iv_test);

		// alpha
		findViewById(R.id.btn_alpha_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_alpha_java");
				ivTest.startAnimation(alphaAnimationHelper.getAnimation());
			}
		});
		findViewById(R.id.btn_alpha_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_alpha_xml");
				ivTest.startAnimation(alphaAnimationHelper.getAnimation(MainActivity.this));
			}
		});

		// rotate
		findViewById(R.id.btn_rotate_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_rotate_java");
				ivTest.startAnimation(rotateAnimationHelper.getAnimation());
			}
		});
		findViewById(R.id.btn_rotate_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_rotate_xml");
				ivTest.startAnimation(rotateAnimationHelper.getAnimation(MainActivity.this));
			}
		});

		// scale
		findViewById(R.id.btn_scale_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_scale_java");
				ivTest.startAnimation(scaleAnimationHelper.getAnimation());
			}
		});
		findViewById(R.id.btn_scale_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_scale_xml");
				ivTest.startAnimation(scaleAnimationHelper.getAnimation(MainActivity.this));
			}
		});

		// translate
		findViewById(R.id.btn_translate_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_translate_java");
				ivTest.startAnimation(translateAnimationHelper.getAnimation());
			}
		});
		findViewById(R.id.btn_translate_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_translate_xml");
				ivTest.startAnimation(translateAnimationHelper.getAnimation(MainActivity.this));
			}
		});

		// animationSet
		findViewById(R.id.btn_animation_set_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_animation_set_java");
				ivTest.startAnimation(animationSetHelper.getAnimationSet());
			}
		});
		findViewById(R.id.btn_animation_set_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_animation_set_xml");
				ivTest.startAnimation(animationSetHelper.getAnimationSet(MainActivity.this));
			}
		});
	}
}
