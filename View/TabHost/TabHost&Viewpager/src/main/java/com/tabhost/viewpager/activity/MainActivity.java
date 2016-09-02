package com.tabhost.viewpager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tabhost.viewpager.R;
import com.tabhost.viewpager.TabPagerFragment;
import com.tabhost.viewpager.TestFragment;
import com.yline.base.BaseFragmentActivity;

public class MainActivity extends BaseFragmentActivity
{
	private TabPagerFragment mTabPagerFragment;

	private FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabPagerFragment = new TabPagerFragment();

		mTabPagerFragment.addTab("one", TestFragment.class);
		mTabPagerFragment.addTab("two", TestFragment.class);
		mTabPagerFragment.addTab("three", TestFragment.class);

		mFragmentTransaction.add(R.id.ll_fragment, mTabPagerFragment).commit();
	}

}
