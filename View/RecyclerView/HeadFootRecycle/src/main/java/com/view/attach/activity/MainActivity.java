package com.view.attach.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.view.attach.R;
import com.view.attach.adapter.HeadFootRecycleAdapter;
import com.view.attach.recycle.DividerLinearItemDecoration;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonRecycleViewHolder;
import com.yline.base.common.CommonRecyclerAdapter;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private RecyclerView recyclerView;

	private HeadFootRecycleAdapter headFootWrapperAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		recyclerView.addItemDecoration(new DividerLinearItemDecoration(this, LinearLayoutManager.VERTICAL));

		List<String> data = new ArrayList<>();
		for (int i = 0; i < 59; i++)
		{
			data.add("item " + i);
		}

		headFootWrapperAdapter = new MainAdapter();
		headFootWrapperAdapter.setOnClickListener(new CommonRecyclerAdapter.OnClickListener()
		{
			@Override
			public void onClick(View view, int i)
			{
				LogFileUtil.v("headFootWrapperAdapter -> position = " + i);
				MainApplication.toast("headFootWrapperAdapter -> position = " + i);
			}
		});

		recyclerView.setAdapter(headFootWrapperAdapter);

		TextView header1 = new TextView(this);
		header1.setText("Header 1");
		TextView header2 = new TextView(this);
		header2.setText("Header 2");
		headFootWrapperAdapter.addHeaderView(header1);
		headFootWrapperAdapter.addHeaderView(header2);

		TextView footer1 = new TextView(this);
		footer1.setText("Footer 1");
		headFootWrapperAdapter.addFootView(footer1);

		TextView footer2 = new TextView(this);
		footer2.setText("Footer 2");
		headFootWrapperAdapter.addFootView(footer2);

		headFootWrapperAdapter.addAll(data);
	}

	private class MainAdapter extends HeadFootRecycleAdapter<String>
	{

		@Override
		public int getItemRes()
		{
			return android.R.layout.simple_list_item_1;
		}

		@Override
		public void setViewContent(CommonRecycleViewHolder viewHolder, int i)
		{
			viewHolder.setText(android.R.id.text1, sList.get(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_recycler_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		switch (id)
		{
			case R.id.action_linear:
				recyclerView.setLayoutManager(new LinearLayoutManager(this));
				break;
			case R.id.action_grid:
				recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
				break;
			case R.id.action_staggered:
				recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
				break;
		}

		recyclerView.setAdapter(headFootWrapperAdapter); // 这里必须从新设置,否则头部会乱掉

		return super.onOptionsItemSelected(item);
	}
}
