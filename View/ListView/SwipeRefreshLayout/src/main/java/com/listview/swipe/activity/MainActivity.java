package com.listview.swipe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.listview.swipe.R;
import com.listview.swipe.RefreshLayout;
import com.yline.base.BaseAppCompatActivity;

import java.util.ArrayList;

/**
 * 很好用,但是效果很单一;仅仅支持ProcessBar的旋转
 */
public class MainActivity extends BaseAppCompatActivity
{
	private RefreshLayout mRefreshLayout;

	private ListView mListView;

	private ArrayAdapter<String> mArrayAdapter;

	private ArrayList<String> values;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		// mRefreshLayout.setFooterView(this, mListView, R.layout.listview_footer);
		mRefreshLayout.setColorSchemeResources(R.color.google_blue,
				R.color.google_green,
				R.color.google_red,
				R.color.google_yellow);

		mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						values.add(0, "Swipe Down to Refresh " + values.size());
						mArrayAdapter.notifyDataSetChanged();
						mRefreshLayout.setRefreshing(false);
					}
				}, 2000);
			}
		});

		/* 添加setLoading 就会出现下滑效果 /
		mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener()
		{
			@Override
			public void onLoad()
			{
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						values.add("Swipe Up to Load More " + values.size());
						mArrayAdapter.notifyDataSetChanged();
						mRefreshLayout.setLoading(false);
					}
				}, 2000);
			}
		});*/

	}

	private void initView()
	{
		mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_container);
		mListView = (ListView) findViewById(R.id.list);

		values = new ArrayList<>();
		for (int i = 0; i < 25; i++)
		{
			values.add("Item " + i);
		}
		mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
		mListView.setAdapter(mArrayAdapter);
	}
}
