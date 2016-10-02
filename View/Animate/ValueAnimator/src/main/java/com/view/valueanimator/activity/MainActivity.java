package com.view.valueanimator.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.view.valueanimator.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private TextView tvTest;

	private RelativeLayout rlTest;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvTest = (TextView) findViewById(R.id.tv_test);
		rlTest = (RelativeLayout) findViewById(R.id.rl_test);

		findViewById(R.id.btn_object_start).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_object_start");
				ObjectActivity.actionStart(MainActivity.this);
			}
		});

		findViewById(R.id.btn_value_animator_simple_start).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_value_animator_simple_start");
				SimpleValueAnimatorActivity.actionStart(MainActivity.this);
			}
		});

		findViewById(R.id.btn_value_animator_color_start).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_value_animator_color_start");
				ColorValueAnimatorActivity.actionStart(MainActivity.this);
			}
		});

		findViewById(R.id.btn_property_animator).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_property_animator");
				ViewPropertyAnimator propertyAnimator = tvTest.animate();
				// BounceInterpolator 表示自由落体效果
				propertyAnimator.x(tvTest.getX()).y(rlTest.getBottom() - rlTest.getPaddingBottom() - tvTest.getHeight()).setDuration(10000).setInterpolator(new BounceInterpolator());
			}
		});
	}

	/**
	 * 获得屏幕高度
	 * @param context
	 * @return such as 1184 if success
	 */
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
}
