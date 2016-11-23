package com.view.pattern.lock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.view.pattern.lock.R;
import com.view.pattern.lock.view.LockPatternHelper;
import com.yline.base.BaseActivity;

/**
 * 引导页面
 */
public class GuideGesturePasswordActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_guide);
		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				LockPatternHelper.getInstance().clearLock();
				CreateGesturePasswordActivity.actionStart(GuideGesturePasswordActivity.this);
				finish();
			}
		});
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, GuideGesturePasswordActivity.class));
	}
}
