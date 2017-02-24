package com.view.design.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.view.design.R;
import com.view.design.fragment.DeleteFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private ViewPager viewPager;

	private TabLayout tabLayout;

	private static final String[] RES = {"tab1", "tab2", "tab3"};

	private List<BaseFragment> fragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		fragmentList = new ArrayList<>();
		for (int i = 0; i < RES.length; i++)
		{
			DeleteFragment deleteFragment = DeleteFragment.newInstance(RES[i]);
			fragmentList.add(deleteFragment);
		}

		FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return RES.length;
			}

			@Override
			public Fragment getItem(int position)
			{
				return fragmentList.get(position);
			}

			@Override
			public CharSequence getPageTitle(int position)
			{
				return RES[position];
			}
		};

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				
			}

			@Override
			public void onPageSelected(int position)
			{
				MainApplication.toast(RES[position]);
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});

		viewPager.setAdapter(pagerAdapter);

		tabLayout.setupWithViewPager(viewPager);
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); // GRAVITY_FILL  尽可能填充  || GRAVITY_CENTER 居中
		tabLayout.setTabMode(TabLayout.MODE_FIXED); // MODE_FIXED 固定Tab || MODE_SCROLLABLE 可滚动tabs
	}

	private void initView()
	{
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		tabLayout = (TabLayout) findViewById(R.id.tab_layout);
	}
}
