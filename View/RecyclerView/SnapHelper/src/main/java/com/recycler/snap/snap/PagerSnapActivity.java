package com.recycler.snap.snap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
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
 * PagerSnapHelper {25.1.0}
 *
 * @author yline 2018/5/15 -- 10:48
 * @version 1.0.0
 */
public class PagerSnapActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, PagerSnapActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private PagerSnapAdapter mSnapAdapter;
    private PagerSnapCallbackHelper mCallbackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.pager_recycler);
        // 布局 管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // 滑动管理
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        // adapter
        mSnapAdapter = new PagerSnapAdapter();
        recyclerView.setAdapter(mSnapAdapter);

        mCallbackHelper = new PagerSnapCallbackHelper();
        mCallbackHelper.attachToRecyclerView(recyclerView);

        initViewClick();
    }

    private void initViewClick() {
        mCallbackHelper.setOnItemSelectListener(new PagerSnapCallbackHelper.OnItemSelectListener() {
            @Override
            public void onItemSelect(int position) {
                mSnapAdapter.notifyVideoPlayer(position);
            }
        });
    }

    private void initData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(UrlConstant.getUrl());
        }

        mSnapAdapter.setDataList(dataList, true);
    }

    private class PagerSnapAdapter extends AbstractCommonRecyclerAdapter<String> {
        private int mPlayPosition = 0;

        @Override
        public int getItemRes() {
            return R.layout.item_recycler;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.setText(R.id.item_recycler_tv, "position = " + position + "\n" + getItem(position));

            FrescoView frescoView = holder.get(R.id.item_recycler_fresco);
            if (position == mPlayPosition) {
                FrescoManager.setImageUri(frescoView, getItem(position));
            } else {
                FrescoManager.setImageResource(frescoView, R.drawable.timg);
            }
        }

        public void notifyVideoPlayer(int position) {
            mPlayPosition = position;
            notifyItemChanged(position);
        }
    }
}
