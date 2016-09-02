package com.tabhost.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.tabhost.viewpager.activity.MainApplication;
import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * TabHost自带空白页,并通过addTab的方式添加Fragment
 *
 * @author yline 2016/9/2 --> 23:58
 * @version 1.0.0
 */
public class TabPagerFragment extends BaseFragment implements OnTabChangeListener, OnPageChangeListener
{
	private TabHost mTabHost;

	private ViewPager mViewPager;

	private TabPagerAdapter mTabPagerAdapter;

	private List<TabPagerHolder> mTabPagerHolderList = new ArrayList<TabPagerHolder>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_tabpager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		if (null != savedInstanceState)
		{
			if (null != mTabHost)
			{
				mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			}
		}

		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.setOnTabChangedListener(this);

		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		// 预加载管理,默认是1;
		mViewPager.setOffscreenPageLimit(3);

		mTabPagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager());
		for (TabPagerHolder holder : mTabPagerHolderList)
		{
			mTabPagerAdapter.addTab(holder.getTabSpec().setContent(new TabContentFactory()
			{

				@Override
				public View createTabContent(String tag)
				{
					View view = new View(getActivity());
					view.setMinimumWidth(0);
					view.setMinimumHeight(0);
					return view;
				}
			}), holder.getClss());
		}
		mViewPager.setAdapter(mTabPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	/**
	 * 新增标题栏,标题
	 *
	 * @param label
	 * @param view  视图
	 * @param cls   fragment.class
	 */
	public void addTab(String label, View view, Class<?> clss)
	{
		mTabPagerHolderList.add(new TabPagerHolder(label, view, clss));
	}

	/**
	 * 新增标题栏,自定义view
	 *
	 * @param tag
	 * @param label 文字
	 * @param clss  fragment.class
	 */
	public void addTab(String label, Class<?> clss)
	{
		mTabPagerHolderList.add(new TabPagerHolder(label, clss));
	}

	/**
	 * 新增标题栏,标题+icon
	 *
	 * @param label
	 * @param id
	 * @param clss
	 */
	public void addTab(String label, int id, Class<?> clss)
	{
		mTabPagerHolderList.add(new TabPagerHolder(label, id, clss));
	}

	private class TabPagerHolder
	{
		private String label;

		private View view;

		private int id;

		private Class<?> clss;

		public TabPagerHolder(String label, Class<?> clss)
		{
			this(label, null, -1, clss);
		}

		public TabPagerHolder(String label, View view, Class<?> clss)
		{
			this(label, view, -1, clss);
		}

		public TabPagerHolder(String label, int id, Class<?> clss)
		{
			this(label, null, id, clss);
		}

		public TabPagerHolder(String label, View view, int id, Class<?> clss)
		{
			super();
			this.label = label;
			this.view = view;
			this.id = id;
			this.clss = clss;
		}

		public TabSpec getTabSpec()
		{
			if (null != view)
			{
				return mTabHost.newTabSpec(label).setIndicator(view);
			}
			else if (-1 != id)
			{
				View defaultView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tabpager_default_type, null);
				((TextView) defaultView.findViewById(R.id.tv_show)).setText(label);
				defaultView.findViewById(R.id.iv_show).setBackgroundResource(id);
				return mTabHost.newTabSpec(label).setIndicator(defaultView);
			}
			else
			{
				return mTabHost.newTabSpec(label).setIndicator(label);
			}
		}

		public Class<?> getClss()
		{
			return clss;
		}
	}

	@Override
	public void onTabChanged(String tabId)
	{
		LogFileUtil.v(MainApplication.TAG, "onTabChanged -> tabId = " + tabId);

		int position = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(position, true);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		LogFileUtil.v(MainApplication.TAG, "onPageScrollStateChanged -> state = " + state);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
	}

	@Override
	public void onPageSelected(int position)
	{
		LogFileUtil.v(MainApplication.TAG, "onPageSelected -> position = " + position);

		TabWidget tabWidget = mTabHost.getTabWidget();
		int oldFocusability = tabWidget.getDescendantFocusability();

		tabWidget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		tabWidget.setDescendantFocusability(oldFocusability);
	}

	private class TabPagerAdapter extends FragmentStatePagerAdapter
	{
		private List<String> classNameList;

		public TabPagerAdapter(FragmentManager fm)
		{
			super(fm);
			classNameList = new ArrayList<String>();
		}

		@Override
		public Fragment getItem(int position)
		{
			return Fragment.instantiate(getActivity(), classNameList.get(position));
		}

		@Override
		public int getCount()
		{
			return classNameList.size();
		}

		/**
		 * 新增tab
		 *
		 * @param tabSpec
		 * @param clss
		 */
		public void addTab(TabSpec tabSpec, Class<?> clss)
		{
			mTabHost.addTab(tabSpec);

			classNameList.add(clss.getName());

			notifyDataSetChanged();
		}
	}

}
