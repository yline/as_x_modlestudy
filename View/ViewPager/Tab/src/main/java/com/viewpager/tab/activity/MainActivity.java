package com.viewpager.tab.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.view.viewpager.tab.R;
import com.viewpager.tab.fragment.ShowFragment;
import com.viewpager.tab.viewhelper.MainBottomHelper;
import com.yline.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity // implements MainBottomHelper.OnTabSelectedListener
{
	private FragmentManager fragmentManager = getSupportFragmentManager();

	private ViewPager viewPager;

	private List<Fragment> fragmentList;

	private MainBottomHelper mainBottomHelper;

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
	}

	private void initView()
	{
		viewPager = (ViewPager) findViewById(R.id.content_viewpager);

		mainBottomHelper = new MainBottomHelper();
		mainBottomHelper.initTabView(findViewById(R.id.include_main_bottom));
		mainBottomHelper.initViewPagerView(viewPager);
		// mainBottomHelper.setListener(this); 突然发现没必要用到
	}

	private void initData()
	{
		fragmentList = new ArrayList<>();
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

		// 这个属于放置数据的地方
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
	}
	/*
	@Override
	public void onTabSelected(int position)
	{
	}*/
}
