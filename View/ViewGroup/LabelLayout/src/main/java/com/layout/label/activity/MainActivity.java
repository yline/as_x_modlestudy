package com.layout.label.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.layout.label.R;
import com.layout.label.fragment.GravityFragment;
import com.layout.label.fragment.ListViewFragment;
import com.layout.label.fragment.MaxCountEachLineFragment;
import com.layout.label.fragment.SampleSimpleFragment;
import com.layout.label.fragment.SelectOneFragment;
import com.layout.label.fragment.SingleClickFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private TabLayout tabLayout;

	private ViewPager viewPager;

	private static final String[] LABEL_TITLES = new String[]{"列表", "简单使用", "每行最多3个", "单个点击", "xml布局", "设定选择1个"};

	private List<BaseFragment> fragmentList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabLayout = (TabLayout) findViewById(R.id.tag_lay);
		viewPager = (ViewPager) findViewById(R.id.viewpager_tab);

		fragmentList.add(new ListViewFragment());
		fragmentList.add(new SampleSimpleFragment());
		fragmentList.add(new MaxCountEachLineFragment());
		fragmentList.add(new SingleClickFragment());
		fragmentList.add(new GravityFragment());
		fragmentList.add(new SelectOneFragment());

		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public Fragment getItem(int position)
			{
				return fragmentList.get(position);
			}

			@Override
			public CharSequence getPageTitle(int position)
			{
				return LABEL_TITLES[position];
			}

			@Override
			public int getCount()
			{
				return LABEL_TITLES.length;
			}
		});
		tabLayout.setupWithViewPager(viewPager);
	}
}
