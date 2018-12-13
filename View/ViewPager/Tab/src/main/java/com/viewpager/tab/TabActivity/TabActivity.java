package com.viewpager.tab.TabActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;

import com.view.viewpager.tab.R;
import com.viewpager.tab.ShowFragment;
import com.yline.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends BaseFragmentActivity {
	public static void launcher(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, TabActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private TabPagerAdapter tabPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab);
		
		initView();
	}
	
	private void initView() {
		ViewPager viewPager = findViewById(R.id.tab_viewpager);
		viewPager.setOffscreenPageLimit(TabBottomView.COUNT);
		
		tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), viewPager);
		viewPager.setAdapter(tabPagerAdapter);
		
		TabBottomView tabBottomView = findViewById(R.id.tab_bottom_view);
		tabBottomView.initViewPagerView(viewPager);
	}
	
	private static class TabPagerAdapter extends FragmentPagerAdapter {
		private ViewPager viewPager;
		private Fragment[] fragmentArray = new Fragment[TabBottomView.COUNT];
		
		private TabPagerAdapter(FragmentManager fm, ViewPager viewPager) {
			super(fm);
			this.viewPager = viewPager;
		}
		
		@Override
		public int getCount() {
			return TabBottomView.COUNT;
		}
		
		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case TabBottomView.FIRST:
					return ShowFragment.getInstance("show1");
				case TabBottomView.SECOND:
					return ShowFragment.getInstance("show2");
				case TabBottomView.THIRD:
					return ShowFragment.getInstance("show3");
				case TabBottomView.FOUR:
					return ShowFragment.getInstance("show4");
				default:
					return ShowFragment.getInstance("show4");
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container, position);
			fragmentArray[position] = fragment;
			return fragment;
		}
		
		/**
		 * 通过这个调用
		 */
		private void refreshFirst(String showText) {
			Fragment fragment = fragmentArray[TabBottomView.FIRST];
			if (null == fragment && null != viewPager) {
				fragment = (Fragment) super.instantiateItem(viewPager, TabBottomView.FIRST);
			}
			if (fragment instanceof ShowFragment) {
				((ShowFragment) fragment).setShowStr(showText);
			}
		}
		
		private void refreshSecond(String showText) {
			Fragment fragment = fragmentArray[TabBottomView.SECOND];
			if (null == fragment && null != viewPager) {
				fragment = (Fragment) super.instantiateItem(viewPager, TabBottomView.SECOND);
			}
			if (fragment instanceof ShowFragment) {
				((ShowFragment) fragment).setShowStr(showText);
			}
		}
		
		private void refreshThird(String showText) {
			Fragment fragment = fragmentArray[TabBottomView.THIRD];
			if (null == fragment && null != viewPager) {
				fragment = (Fragment) super.instantiateItem(viewPager, TabBottomView.THIRD);
			}
			if (fragment instanceof ShowFragment) {
				((ShowFragment) fragment).setShowStr(showText);
			}
		}
		
		private void refreshFour(String showText) {
			Fragment fragment = fragmentArray[TabBottomView.FOUR];
			if (null == fragment && null != viewPager) {
				fragment = (Fragment) super.instantiateItem(viewPager, TabBottomView.FOUR);
			}
			if (fragment instanceof ShowFragment) {
				((ShowFragment) fragment).setShowStr(showText);
			}
		}
	}
}
