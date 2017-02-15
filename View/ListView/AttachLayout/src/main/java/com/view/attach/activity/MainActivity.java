package com.view.attach.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.view.attach.R;
import com.view.attach.viewhelper.MainHelper;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView()
	{
		MainHelper mainHelper = new MainHelper();
		listView = (ListView) findViewById(R.id.lv_content);

		// listView必须要设置完adapter, head 和 foot才会生效
		View headView = LayoutInflater.from(this).inflate(R.layout.activity_main_head, null);
		listView.addHeaderView(headView);

		View footView = LayoutInflater.from(this).inflate(R.layout.activity_main_foot, null);
		listView.addFooterView(footView);

		mainHelper.initListView(listView);
		mainHelper.initHeadView(headView);
		mainHelper.initFootView(footView);
	}

	private void initData()
	{
		CommonListAdapter adapter = new CommonListAdapter<String>(this)
		{
			@Override
			protected int getItemRes(int i)
			{
				return android.R.layout.simple_list_item_1;
			}

			@Override
			protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
			{
				viewHolder.setText(android.R.id.text1, sList.get(i));
			}
		};

		listView.setAdapter(adapter);

		List<String> data = new ArrayList<>();
		for (int i = 0; i < 30; i++)
		{
			data.add("item = " + i);
		}
		adapter.set(data);
	}
}
