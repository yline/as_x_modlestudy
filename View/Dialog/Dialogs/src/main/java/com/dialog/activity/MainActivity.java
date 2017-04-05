package com.dialog.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.dialog.fragment.ButtonDialogFragment;
import com.dialog.fragment.CustomDialogFragment;
import com.dialog.fragment.ListDialogFragment;
import com.dialog.fragment.ProgressDialogFragment;
import com.view.dialog.R;
import com.yline.base.BaseFragment;
import com.yline.test.BaseTestActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTestActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final List<BaseFragment> fragmentList = new ArrayList<>();
		final List<String> titleList = new ArrayList<>();

		fragmentList.add(new ButtonDialogFragment());
		titleList.add("选择");

		fragmentList.add(new ListDialogFragment());
		titleList.add("列表");
		
		fragmentList.add(new ProgressDialogFragment());
		titleList.add("进度");

		fragmentList.add(new CustomDialogFragment());
		titleList.add("自定义");

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
		tabLayout.setTabTextColors(getResources().getColor(android.R.color.black), getResources().getColor(android.R.color.holo_red_light));
	}
}
