package com.view.recycler.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.view.recycler.R;
import com.view.recycler.adapter.CommonRecyclerAdapter;
import com.view.recycler.adapter.CommonRecyclerViewHolder;
import com.view.recycler.decoration.DividerGridItemDecoration;
import com.view.recycler.decoration.DividerLinearItemDecoration;
import com.yline.base.BaseAppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends BaseAppCompatActivity
{
	private RecyclerView recyclerView;

	private ArrayList<Bean> data;

	private RecyclerView.ItemDecoration decor;

	private HomeAdapter homeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = (RecyclerView) findViewById(R.id.recycler);
		homeAdapter = new HomeAdapter();
		recyclerView.setAdapter(homeAdapter);
		// 添加动画效果
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		initData();

		// 线性布局
		findViewById(R.id.btn_linear).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

				// 添加新的一条前,移除旧的,以免叠加
				recyclerView.removeItemDecoration(decor);
				decor = new DividerLinearItemDecoration(MainActivity.this, DividerLinearItemDecoration.VERTICAL_LIST);
				recyclerView.setItemAnimator(new DefaultItemAnimator());
				recyclerView.addItemDecoration(decor, 0);
			}
		});

		// 类似GridView
		findViewById(R.id.btn_grid).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));

				// 添加新的一条前,移除旧的,以免叠加
				recyclerView.removeItemDecoration(decor);
				decor = new DividerGridItemDecoration(MainActivity.this);
				recyclerView.addItemDecoration(decor, 0);
			}
		});

		// 可换方向的GridView
		findViewById(R.id.btn_staggered_grid).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));

				// 添加新的一条前,移除旧的,以免叠加
				recyclerView.removeItemDecoration(decor);
				decor = new DividerGridItemDecoration(MainActivity.this);
				recyclerView.addItemDecoration(decor, 0);
			}
		});

		// 增加、减少数据点击事件
		findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				homeAdapter.add(1, new Bean("insert data", 200 + random.nextInt(100)));
			}
		});

		findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				homeAdapter.remove(1);
			}
		});

		homeAdapter.setOnClickListener(new CommonRecyclerAdapter.OnClickListener()
		{
			@Override
			public void onClick(View v, int position)
			{
				MainApplication.toast(homeAdapter.getItemData(position).getContent());
			}
		});
	}

	private Random random;

	private void initData()
	{
		random = new Random();
		data = new ArrayList<>();
		for (int i = 'A'; i < 'z'; i++)
		{
			data.add(new Bean("" + (char) i, 200 + random.nextInt(100)));
		}
		homeAdapter.addAll(data);
	}

	private class Bean
	{
		public Bean(String content, int height)
		{
			this.content = content;
			this.height = height;
		}

		private String content;

		private int height;

		public String getContent()
		{
			return content;
		}

		public void setContent(String content)
		{
			this.content = content;
		}

		public int getHeight()
		{
			return height;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}
	}

	private class HomeAdapter extends CommonRecyclerAdapter<Bean>
	{

		@Override
		public int getItemRes()
		{
			return R.layout.item_home;
		}

		@Override
		public void setViewContent(CommonRecyclerViewHolder holder, int position)
		{
			holder.setText(R.id.tv_num, sList.get(position).getContent());
			holder.setLayout(R.id.fl_item, -1, sList.get(position).getHeight());
		}
	}
}
