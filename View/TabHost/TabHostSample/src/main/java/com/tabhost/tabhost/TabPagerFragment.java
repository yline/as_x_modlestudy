package com.tabhost.tabhost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.tabhost.R;
import com.tabhost.activity.MainApplication;
import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yline on 2016/9/1.
 */
public class TabPagerFragment extends BaseFragment implements TabHost.OnTabChangeListener
{
	private TabHost mTabHost;

	private List<String> mList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_tabpager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);

		mList.add("one");
		mList.add("two");
		mList.add("three");

		mTabHost.setup();
		mTabHost.setOnTabChangedListener(this);

		for (int i = 0; i < mList.size(); i++)
		{
			mTabHost.addTab(mTabHost.newTabSpec(mList.get(i)).setIndicator(mList.get(i)).setContent(new TabHost.TabContentFactory()
			{

				@Override
				public View createTabContent(String tag)
				{
					TextView view = new TextView(getActivity());
					view.setText("tag -> " + tag);
					return view;
				}
			}));
		}
	}

	@Override
	public void onTabChanged(String tabId)
	{
		LogFileUtil.v(MainApplication.TAG, "onTabChanged -> tabId = " + tabId);
		LogFileUtil.v(MainApplication.TAG, "onTabChanged -> CurrentTab = " + mTabHost.getCurrentTab());
		LogFileUtil.v(MainApplication.TAG, "onTabChanged -> CurrentTabTag = " + mTabHost.getCurrentTabTag());
	}

}

