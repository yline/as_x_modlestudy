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
import com.layout.label.fragment.SingleClickFragment;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	private TabLayout tabLayout;

	private ViewPager viewPager;

	private static final String[] LABEL_TITLES = new String[]{"列表", "简单使用", "每行最多3个", "单个点击", "xml布局"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabLayout = (TabLayout) findViewById(R.id.tag_lay);
		viewPager = (ViewPager) findViewById(R.id.viewpager_tab);

		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public Fragment getItem(int position)
			{
				switch (position)
				{
					case 0:
						return new ListViewFragment();
					case 1:
						return new SampleSimpleFragment();
					case 2:
						return new MaxCountEachLineFragment();
					case 3:
						return new SingleClickFragment();
					case 4:
						return new GravityFragment();
					default:
						return new ListViewFragment();
				}
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
