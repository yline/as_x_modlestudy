package com.sqlite.page.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.sqlite.page.R;
import com.sqlite.page.bean.Person;
import com.sqlite.page.helper.DbManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private ListView lvLimit;

	private LimitAdapter limitAdapter;

	private List<Person> dataList;

	/** 是否到达 当前最后一位 */
	private boolean isDivPage = false;

	private int currentPage = 0, dataTotalPage;

	private long dataTotalSize;

	/** 这个必须大于 每页的最多加载的数据 */
	private static final int PAGE_SIZE = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DbManager.getInstance().insertAtSameMoment(0, "yline", 21, 200);
		
		initView();

		dataList = new ArrayList<>();
		dataList = DbManager.getInstance().queryAllLimit(currentPage, PAGE_SIZE);
		currentPage++;

		limitAdapter.addAll(dataList);

		dataTotalSize = DbManager.getInstance().getCountSize();
		dataTotalPage = (int) Math.ceil(dataTotalSize * 1.0f / PAGE_SIZE);

		LogFileUtil.v("tag", "currentPage = " + currentPage + ",dataTotalPage = " + dataTotalPage + ",dataTotalSize = " + dataTotalSize);

		lvLimit.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if (isDivPage && SCROLL_STATE_IDLE == scrollState)
				{
					LogFileUtil.v("tag", "currentPage = " + currentPage);
					if (currentPage < dataTotalPage)
					{
						limitAdapter.addAll(DbManager.getInstance().queryAllLimit(currentPage, PAGE_SIZE));
						currentPage++;
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				// 再到达最后一个item之前,2个,就进行数据更新
				isDivPage = ((firstVisibleItem + visibleItemCount) >= totalItemCount - 2);
			}
		});
	}
	
	private void initView()
	{
		lvLimit = (ListView) findViewById(R.id.lv_limit);

		limitAdapter = new LimitAdapter(this);

		lvLimit.setAdapter(limitAdapter);
	}

	private class LimitAdapter extends CommonListAdapter<Person>
	{
		public LimitAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_limit;
		}

		@Override
		protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			viewHolder.setText(R.id.tv_id, sList.get(i).getId() + "");
			viewHolder.setText(R.id.tv_name, sList.get(i).getName());
			viewHolder.setText(R.id.tv_age, sList.get(i).getAge() + "");
		}
	}

}
