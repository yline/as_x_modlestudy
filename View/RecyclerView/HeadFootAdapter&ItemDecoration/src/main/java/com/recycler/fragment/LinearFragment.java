package com.recycler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recycler.R;
import com.recycler.adapter.HeadFootRecyclerAdapter;
import com.recycler.decoration.DefaultLinearItemDecoration;
import com.yline.base.BaseFragment;
import com.yline.common.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LinearFragment extends BaseFragment
{
	private HeadFootRecyclerAdapter headFootRecyclerAdapter;

	public static LinearFragment newInstance()
	{
		return new LinearFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_linear, container, false);
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

		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_linear);
		recyclerView.setAdapter(headFootRecyclerAdapter);
		recyclerView.addItemDecoration(new DefaultLinearItemDecoration(getContext())
		{
			@Override
			protected int getNonDivideHeadNumber()
			{
				return 2;
			}

			@Override
			protected int getNonDivideFootNumber()
			{
				return 2;
			}

			@Override
			protected int getDividerResourceId()
			{
				return R.drawable.widget_recycler_divider_white_small;
			}
		});
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		List<String> dataList = new ArrayList<>();
		for (int i = 0; i < 20; i++)
		{
			dataList.add("fucker - " + i);
		}
		headFootRecyclerAdapter.setDataList(dataList);

		headFootRecyclerAdapter.addHeadView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
		headFootRecyclerAdapter.addHeadView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
		headFootRecyclerAdapter.addFootView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
		headFootRecyclerAdapter.addFootView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
	}
}
