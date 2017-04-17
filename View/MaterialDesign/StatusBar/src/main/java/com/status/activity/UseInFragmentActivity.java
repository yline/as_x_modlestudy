package com.status.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.status.R;
import com.status.fragment.ImageFragment;
import com.status.fragment.SimpleFragment;
import com.status.util.StatusBarUtil;
import com.yline.base.BaseAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UseInFragmentActivity extends BaseAppCompatActivity
{
	private ArrayList<Fragment> mFragmentList = new ArrayList<>();

	private List<String> mTitleList = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_in_fragment);
		
		// 设置 侵入到 状态栏
		StatusBarUtil.setTranslucentForImageViewInFragment(UseInFragmentActivity.this, null);

		mFragmentList.add(new ImageFragment());
		mTitleList.add("one");
		mFragmentList.add(new SimpleFragment());
		mTitleList.add("two");
		mFragmentList.add(new SimpleFragment());
		mTitleList.add("three");
		mFragmentList.add(new SimpleFragment());
		mTitleList.add("four");

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageSelected(int position)
			{
				Random random = new Random();
				int color = 0xff000000 | random.nextInt(0xffffff);
				if (mFragmentList.get(position) instanceof SimpleFragment)
				{
					((SimpleFragment) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public Fragment getItem(int position)
			{
				return mFragmentList.get(position);
			}

			@Override
			public int getCount()
			{
				return mFragmentList.size();
			}

			@Override
			public CharSequence getPageTitle(int position)
			{
				return mTitleList.get(position);
			}
		});
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.setTabTextColors(getResources().getColor(android.R.color.black), getResources().getColor(android.R.color.holo_red_light));
		tabLayout.setSelectedTabIndicatorHeight(0);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, UseInFragmentActivity.class));
	}
}
