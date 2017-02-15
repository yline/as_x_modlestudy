package com.listview.swipe.viewhelper;

import android.support.v4.widget.SwipeRefreshLayout;

import com.listview.swipe.activity.MainApplication;

/**
 * Created by yline on 2017/2/15.
 */
public class SampleHelper
{
	private static final int[] COLOR = new int[]{android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light};

	public void init(final SwipeRefreshLayout swipeRefreshLayout)
	{
		swipeRefreshLayout.setColorSchemeResources(COLOR);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				MainApplication.toast("正在加载");
				MainApplication.getHandler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						MainApplication.toast("刷新结束");
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 6000);
			}
		});
	}
}
