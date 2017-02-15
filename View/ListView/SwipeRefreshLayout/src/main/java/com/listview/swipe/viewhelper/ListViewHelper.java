package com.listview.swipe.viewhelper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

import com.listview.swipe.activity.MainApplication;

public class ListViewHelper
{
	private static final int[] COLOR = new int[]{android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light};

	private OnRefreshListener listener;

	private SwipeRefreshLayout swipeRefreshLayout;

	public void initSwipeRefresh(final SwipeRefreshLayout swipeRefreshLayout)
	{
		this.swipeRefreshLayout = swipeRefreshLayout;
		swipeRefreshLayout.setColorSchemeResources(COLOR);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				MainApplication.getHandler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						// 更新数据
						if (null != listener)
						{
							listener.onFinish();
						}
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 2000);
			}
		});
	}

	public void initListView(final ListView listView)
	{
		if (null != swipeRefreshLayout)
		{
			initListViewScroll(listView);
		}
	}

	/**
	 * 解决ListView 和 swipeRefreshLayout 的冲突问题
	 * @param listView
	 */
	private void initListViewScroll(final ListView listView)
	{
		listView.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				boolean isAble = false;
				if (listView != null && listView.getChildCount() > 0)
				{
					// check if the first item of the list is visible
					boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
					// check if the top of the first item is visible
					boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
					// enabling or disabling the refresh layout
					isAble = firstItemVisible && topOfFirstItemVisible;
				}
				swipeRefreshLayout.setEnabled(isAble);
			}
		});
	}

	public void setListener(OnRefreshListener listener)
	{
		this.listener = listener;
	}

	public interface OnRefreshListener
	{
		void onFinish();
	}
}
