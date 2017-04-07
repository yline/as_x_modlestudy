package com.recycler.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.recycler.R;
import com.recycler.fragment.GridFragment;
import com.recycler.fragment.LinearFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final List<BaseFragment> fragmentList = new ArrayList<>();
		final List<String> titleList = new ArrayList<>();

		fragmentList.add(LinearFragment.newInstance());
		titleList.add("线性");

		fragmentList.add(GridFragment.newInstance());
		titleList.add("多列");

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public Fragment getItem(int position)
			{
				return fragmentList.get(position);
			}

			@Override
			public int getCount()
			{
				return fragmentList.size();
			}

			@Override
			public CharSequence getPageTitle(int position)
			{
				return titleList.get(position);
			}
		});
		tabLayout.setupWithViewPager(viewPager);
	}
}
