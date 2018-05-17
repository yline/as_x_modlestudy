package com.recycler.snap.snap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.recycler.snap.R;
import com.yline.base.BaseActivity;
import com.yline.test.UrlConstant;
import com.yline.view.fresco.FrescoManager;
import com.yline.view.fresco.view.FrescoView;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * LinearSnapHelper {24.2.0}
 *
 * @author yline 2018/5/15 -- 10:46
 * @version 1.0.0
 */
public class LinearSnapActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, LinearSnapActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private LinearSnapAdapter mSnapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.linear_recycler);
        // 布局 管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // 滑动管理
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        // adapter
        mSnapAdapter = new LinearSnapAdapter();
        recyclerView.setAdapter(mSnapAdapter);
    }

    private void initData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(UrlConstant.getUrl());
        }

        mSnapAdapter.setDataList(dataList, true);
    }

    private class LinearSnapAdapter extends AbstractCommonRecyclerAdapter<String> {

        @Override
        public int getItemRes() {
            return R.layout.item_recycler;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            FrescoView frescoView = holder.get(R.id.item_recycler_fresco);
            FrescoManager.setImageUri(frescoView, getItem(position));

            holder.setText(R.id.item_recycler_tv, "position = " + position + "\n" + getItem(position));
        }
    }
}
