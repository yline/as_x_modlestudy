package com.view.viewpager.pageradapter.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.view.viewpager.pageradapter.R;
import com.view.viewpager.pageradapter.adapter.ViewPagerAdapter;
import com.yline.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
{
	private ViewPager viewPager;

	private ViewPagerAdapter viewPagerAdapter;

	private List<View> mViewList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化控件
		initView();

		for (int i = 0; i < 10; i++)
		{
			final String tempText = MainApplication.TAG + i;
			TextView textView = new TextView(MainActivity.this);
			textView.setText(tempText);
			mViewList.add(textView);
			textView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					MainApplication.toast(tempText);
				}
			});
		}

		// 放入数据
		viewPagerAdapter.setViews(mViewList);
	}

	private void initView()
	{
		viewPager = (ViewPager) findViewById(R.id.viewpager_drag);
		viewPagerAdapter = new ViewPagerAdapter(MainActivity.this);
		mViewList = new ArrayList<View>();

		viewPager.setAdapter(viewPagerAdapter);
	}
}
