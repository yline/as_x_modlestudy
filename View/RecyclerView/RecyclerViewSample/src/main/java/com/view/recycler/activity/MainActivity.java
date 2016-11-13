package com.view.recycler.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.view.recycler.R;
import com.yline.base.BaseAppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends BaseAppCompatActivity
{
	private RecyclerView recyclerView;

	private ArrayList<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = (RecyclerView) findViewById(R.id.recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
		recyclerView.setAdapter(new HomeAdapter());

		initData();
	}

	private void initData()
	{
		data = new ArrayList<>();
		for (int i = 'A'; i < 'z'; i++)
		{
			data.add("" + (char) i);
		}
	}

	private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>
	{

		@Override
		public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_home, parent, false);
			HomeViewHolder viewHolder = new HomeViewHolder(view);
			return viewHolder;
		}

		@Override
		public int getItemCount()
		{
			return data.size();
		}

		@Override
		public void onBindViewHolder(HomeViewHolder holder, int position)
		{
			holder.setText(R.id.tv_num, data.get(position));
		}
	}

	private class HomeViewHolder extends RecyclerView.ViewHolder
	{
		private TextView tvNum;

		public HomeViewHolder(View itemView)
		{
			super(itemView);
		}

		public HomeViewHolder setText(int viewId, String content)
		{
			if (null == tvNum)
			{
				tvNum = (TextView) itemView.findViewById(viewId);
			}
			tvNum.setText(content);
			return this;
		}
	}
}
