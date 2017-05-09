package com.floating.recycler;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.common.CommonRecyclerAdapter;
import com.yline.common.CommonRecyclerViewHolder;
import com.yline.utils.UIScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends BaseAppCompatActivity
{
	private List<String> list = new ArrayList<>();//adapter数据源

	private Map<Integer, String> keys = new HashMap<>();//存放所有key的位置和内容

	private MainAdapter adapter;

	private RecyclerView rv;

	private Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rv = (RecyclerView) findViewById(R.id.rv);

		for (int i = 0; i < 6; i++)
		{
			keys.put(list.size(), "我是第" + i + "个标题");
			int maxJ = random.nextInt(10) + 1;
			for (int j = 0; j < maxJ; j++)
			{
				list.add("第" + (j + 1) + "个item，我属于标题" + i);
			}
		}
		
		//设置adapter
		adapter = new MainAdapter();

		final FloatingItemDecoration floatingItemDecoration = new FloatingItemDecoration(this, Color.WHITE, 1, 3);
		floatingItemDecoration.setKeys(keys);
		floatingItemDecoration.setTitleHeight(UIScreenUtil.dp2px(this, 30));
		rv.addItemDecoration(floatingItemDecoration);

		//设置布局管理器
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(adapter);

		adapter.setDataList(list);
	}

	private class MainAdapter extends CommonRecyclerAdapter<String>
	{
		@Override
		public int getItemRes()
		{
			return R.layout.item_main;
		}

		@Override
		public void onBindViewHolder(CommonRecyclerViewHolder viewHolder, int position)
		{
			viewHolder.setText(R.id.tv, sList.get(position));
		}
	}
}
