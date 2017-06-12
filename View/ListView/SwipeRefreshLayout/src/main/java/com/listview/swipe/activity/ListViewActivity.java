package com.listview.swipe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.widget.ListView;

import com.listview.swipe.R;
import com.listview.swipe.viewhelper.ListViewHelper;
import com.yline.base.BaseAppCompatActivity;
import com.yline.view.common.CommonListAdapter;
import com.yline.view.common.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListViewActivity extends BaseAppCompatActivity implements ListViewHelper.OnRefreshListener
{
	private ListView mListView;
	
	private CommonListAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		initView();
	}
	
	private void initView()
	{
		ListViewHelper listViewHelper = new ListViewHelper();
		listViewHelper.initSwipeRefresh((SwipeRefreshLayout) findViewById(R.id.swipe_container));

		mListView = (ListView) findViewById(R.id.lv_list);
		
		List<String> values = new ArrayList<>();
		for (int i = 0; i < 25; i++)
		{
			values.add("Item " + i);
		}

		adapter = new CommonListAdapter<String>(ListViewActivity.this)
		{
			@Override
			protected int getItemRes(int i)
			{
				return android.R.layout.simple_list_item_1;
			}

			@Override
			protected void onBindViewHolder(ViewGroup parent, ViewHolder viewHolder, int position)
			{
				viewHolder.setText(android.R.id.text1, sList.get(position));
			}
		};
		mListView.setAdapter(adapter);
		adapter.addAll(values);

		listViewHelper.initListView(mListView);
		listViewHelper.setListener(this);
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, ListViewActivity.class));
	}

	@Override
	public void onFinish()
	{
		adapter.add(0, "Swipe Down to Refresh " + new Random().nextInt(100));
	}
}
