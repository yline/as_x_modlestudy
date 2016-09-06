package com.viewpager.indicatorhelper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpager.indicatorhelper.R;
import com.viewpager.indicatorhelper.helper.ViewPagerHelper;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	private static final String TAG = "MainActivity";

	private ViewPager viewPager;

	private ViewPagerHelper viewPagerHelper;

	private LinearLayout llIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.viewpager_drag);
		llIndicator = (LinearLayout) findViewById(R.id.ll_indicator);

		viewPagerHelper = new ViewPagerHelper(MainActivity.this, viewPager);
		viewPagerHelper.setIndicator(llIndicator);
		viewPagerHelper.create();

		final EditText numberStr = (EditText) findViewById(R.id.et_input);

		findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				int number = Integer.parseInt(numberStr.getText().toString().trim());
				int oldCount = viewPagerHelper.getCount();
				if (number > 0)
				{
					if (number > oldCount)
					{
						for (int i = 0; i < number - oldCount; i++)
						{
							TextView text = new TextView(MainActivity.this);
							text.setTextColor(Color.BLUE);
							text.setText("item" + (oldCount + i));
							viewPagerHelper.addView(text);
						}
					}
					else if (number == oldCount)
					{
						// do nothing
					}
					else
					{
						for (int i = oldCount; i > number; i--)
						{
							viewPagerHelper.removeView(i - 1);
						}
					}
				}
				else
				{
					LogFileUtil.e(TAG, "input is error");
				}
			}
		});
	}
}
