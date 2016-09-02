package com.tabhost.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tabhost.R;
import com.tabhost.tabhost.TabPagerFragment;
import com.yline.base.BaseFragmentActivity;

public class MainActivity extends BaseFragmentActivity
{
	private TabPagerFragment mTabPagerFragment;

	private FragmentManager mFragmentManager = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabPagerFragment = new TabPagerFragment();

		mFragmentManager.beginTransaction().add(R.id.ll_fragment, mTabPagerFragment).commit();
	}
}
