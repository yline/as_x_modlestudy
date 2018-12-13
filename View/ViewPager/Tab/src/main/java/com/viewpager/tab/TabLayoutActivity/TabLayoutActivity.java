package com.viewpager.tab.TabLayoutActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.view.viewpager.tab.R;
import com.viewpager.tab.ShowFragment;
import com.viewpager.tab.TabActivity.TabBottomView;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {
	public static void launcher(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, TabLayoutActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private TabLayoutPagerAdapter pagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_layout);
		
		initView();
	}
	
	private void initView() {
		ViewPager viewPager = findViewById(R.id.tab_layout_viewpager);
		pagerAdapter = new TabLayoutPagerAdapter(getSupportFragmentManager(), viewPager);
		viewPager.setOffscreenPageLimit(TabLayoutPagerAdapter.COUNT);
		viewPager.setAdapter(pagerAdapter);
		
		TabLayout tabLayout = findViewById(R.id.tab_layout_tab);
		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		tabLayout.setTabTextColors(ContextCompat.getColor(this, android.R.color.black), ContextCompat.getColor(this, android.R.color.holo_red_light));
		tabLayout.setupWithViewPager(viewPager);
		
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int position = tab.getPosition();
				switch (position) {
					case TabLayoutPagerAdapter.FIRST:
						pagerAdapter.refreshFirst("refresh " + position);
						break;
					case TabLayoutPagerAdapter.SECOND:
						pagerAdapter.refreshSecond("refresh " + position);
						break;
					case TabLayoutPagerAdapter.THIRD:
						pagerAdapter.refreshThird("refresh " + position);
						break;
					case TabLayoutPagerAdapter.FOUR:
						pagerAdapter.refreshFour("refresh " + position);
						break;
				}
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			
			}
		});
	}
	
	private static class TabLayoutPagerAdapter extends FragmentPagerAdapter {
		private static final int FIRST = 0;
		private static final int SECOND = 1;
		private static final int THIRD = 2;
		private static final int FOUR = 3;
		
		private static final int COUNT = 4;
		
		private ViewPager viewPager;
		private Fragment[] fragmentArray = new Fragment[COUNT];
		
		private TabLayoutPagerAdapter(FragmentManager fm, ViewPager viewPager) {
			super(fm);
			this.viewPager = viewPager;
		}
		
		@Override
		public int getCount() {
			return COUNT;
		}
		
		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case FIRST:
					return ShowFragment.getInstance("show1");
				case SECOND:
					return ShowFragment.getInstance("show2");
				case THIRD:
					return ShowFragment.getInstance("show3");
				case FOUR:
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
		
		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			return "Tab" + position;
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
