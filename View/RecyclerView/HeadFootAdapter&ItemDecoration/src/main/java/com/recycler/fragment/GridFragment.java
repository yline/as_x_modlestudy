package com.recycler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recycler.R;
import com.recycler.adapter.HeadFootRecyclerAdapter;
import com.recycler.decoration.DefaultGridItemDecoration;
import com.yline.base.BaseFragment;
import com.yline.common.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GridFragment extends BaseFragment
{
	private HeadFootRecyclerAdapter headFootRecyclerAdapter;

	public static GridFragment newInstance()
	{
		return new GridFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_grid, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		headFootRecyclerAdapter = new HeadFootRecyclerAdapter<String>()
		{
			@Override
			public int getItemRes()
			{
				return R.layout.item_recycler;
			}

			@Override
			public void setViewContent(CommonRecyclerViewHolder holder, int position)
			{
				holder.setText(R.id.tv_num, sList.get(position));
			}
		};

		headFootRecyclerAdapter.addHeadView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
		headFootRecyclerAdapter.addHeadView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
		headFootRecyclerAdapter.addFootView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
		headFootRecyclerAdapter.addFootView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));

		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_grid);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 这个必须在 setAdapter之前执行，否则添加错误
		recyclerView.setAdapter(headFootRecyclerAdapter);
		recyclerView.addItemDecoration(new DefaultGridItemDecoration(getContext())
		{
			@Override
			protected int getHeadNumber()
			{
				return 2;
			}

			@Override
			protected int getFootNumber()
			{
				return 2;
			}

			@Override
			protected boolean isDivideLastLine()
			{
				return false;
			}

			@Override
			protected int getDivideResourceId()
			{
				return R.drawable.widget_recycler_divider_white_small;
			}
		});

		List<String> dataList = new ArrayList<>();
		for (int i = 0; i < 20; i++)
		{
			dataList.add("fucker - " + i);
		}
		headFootRecyclerAdapter.setDataList(dataList);
	}
}
