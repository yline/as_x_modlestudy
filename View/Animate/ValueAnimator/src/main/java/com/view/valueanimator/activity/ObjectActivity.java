package com.view.valueanimator.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.view.valueanimator.R;
import com.view.valueanimator.helper.ObjectAnimatorHelper;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/10/2.
 */
public class ObjectActivity extends BaseAppCompatActivity
{
	private static final String TAG = "ObjectAnimator";

	private TextView tvTest;

	private ObjectAnimatorHelper objectAnimatorHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object);
		
		initView();
		initData();
	}
	
	private void initData()
	{
		objectAnimatorHelper = new ObjectAnimatorHelper();
	}
	
	private void initView()
	{
		tvTest = (TextView) findViewById(R.id.tv_test);
		// alpha
		findViewById(R.id.btn_alpha_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_alpha_java");
				objectAnimatorHelper.actionAlpha(tvTest);
			}
		});
		findViewById(R.id.btn_alpha_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_alpha_java");
				objectAnimatorHelper.actionAlpha(ObjectActivity.this, tvTest);
			}
		});

		// rotate
		findViewById(R.id.btn_rotate_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_rotate_java");
				objectAnimatorHelper.actionRotate(tvTest);
			}
		});
		findViewById(R.id.btn_rotate_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_rotate_xml");
				objectAnimatorHelper.actionRotate(ObjectActivity.this, tvTest);
			}
		});

		// scale
		findViewById(R.id.btn_scale_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_scale_java");
				objectAnimatorHelper.actionScale(ObjectActivity.this, tvTest);
			}
		});
		findViewById(R.id.btn_scale_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_scale_xml");
				objectAnimatorHelper.actionScale(ObjectActivity.this, tvTest);
			}
		});

		// translate
		findViewById(R.id.btn_translate_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_translate_java");
				objectAnimatorHelper.actionTranslate(tvTest);
			}
		});
		findViewById(R.id.btn_translate_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_translate_xml");
				objectAnimatorHelper.actionTranslate(ObjectActivity.this, tvTest);
			}
		});

		// set
		findViewById(R.id.btn_animation_set_java).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_animation_set_java");
				objectAnimatorHelper.actionAnimatorSet(tvTest);
			}
		});
		findViewById(R.id.btn_animation_set_xml).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_animation_set_xml");
				objectAnimatorHelper.actionAnimatorSet(ObjectActivity.this, tvTest);
			}
		});
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, ObjectActivity.class));
	}
}
