package com.view.recycler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.view.recycler.R;
import com.yline.base.BaseFragment;
import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.decoration.CommonGridDecoration;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GridFragment extends BaseFragment {
    private AbstractHeadFootRecyclerAdapter<String> mHeadFootRecyclerAdapter;

    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeadFootRecyclerAdapter = new AbstractHeadFootRecyclerAdapter<String>() {
            @Override
            public void onBindViewHolder(RecyclerViewHolder holder, int position) {
                holder.setText(R.id.tv_num, getItem(position));
            }

            @Override
            public int getItemRes() {
                return R.layout.item_recycler;
            }
        };

        mHeadFootRecyclerAdapter.addHeadView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
        mHeadFootRecyclerAdapter.addHeadView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
        mHeadFootRecyclerAdapter.addFootView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));
        mHeadFootRecyclerAdapter.addFootView(LayoutInflater.from(getContext()).inflate(R.layout.item_attach, null));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_grid);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 这个必须在 setAdapter之前执行，否则添加错误
        recyclerView.setAdapter(mHeadFootRecyclerAdapter);
        recyclerView.addItemDecoration(new CommonGridDecoration(getContext()) {
            @Override
            protected int getDivideResourceId() {
                return R.drawable.widget_recycler_divider_white_small;
            }
        });

        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add("fucker - " + i);
        }
        mHeadFootRecyclerAdapter.setDataList(dataList, true);
    }
}
