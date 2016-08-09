package com.viewpager.tab.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

import com.view.viewpager.tab.R;
import com.viewpager.tab.fragment.ShowFragment;
import com.viewpager.tab.styleline.TabLineFragment;
import com.yline.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements TabLineFragment.OnTabSelectedListener
{
	private FragmentManager fragmentManager = getSupportFragmentManager();

	private ViewPager viewPager;

	private List<Fragment> fragmentList;

	private TabLineFragment lineFragment;

	private ShowFragment show1;

	private ShowFragment show2;

	private ShowFragment show3;

	private ShowFragment show4;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initData();

		fragmentManager.beginTransaction().add(R.id.show_frame, lineFragment, "tabline").commit();
	}

	private void initView()
	{
		viewPager = (ViewPager) findViewById(R.id.content_viewpager);
	}

	private void initData()
	{
		lineFragment = new TabLineFragment();

		fragmentList = new ArrayList<Fragment>();
		show1 = new ShowFragment();
		show1.setShowStr("show1");
		show2 = new ShowFragment();
		show2.setShowStr("show2");
		show3 = new ShowFragment();
		show3.setShowStr("show3");
		show4 = new ShowFragment();
		show4.setShowStr("show4");

		fragmentList.add(show1);
		fragmentList.add(show2);
		fragmentList.add(show3);
		fragmentList.add(show4);

		viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager)
		{

			@Override
			public int getCount()
			{
				return fragmentList.size();
			}

			@Override
			public Fragment getItem(int position)
			{
				return fragmentList.get(position);
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				lineFragment.setTextColor(position);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPx)
			{
				lineFragment.moveTabLine(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int position)
			{

			}
		});
	}

	@Override
	public void onTabSelected(int position)
	{
		viewPager.setCurrentItem(position);
	}
}
