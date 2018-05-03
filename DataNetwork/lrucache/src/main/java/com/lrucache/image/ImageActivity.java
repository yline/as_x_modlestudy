package com.lrucache.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.lrucache.IApplication;
import com.lrucache.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;
import com.yline.test.UrlConstant;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示 ImageLoader 加载网络图片
 *
 * @author yline 2018/5/3 -- 13:42
 * @version 1.0.0
 */
public class ImageActivity extends BaseActivity {
    private ImageRecyclerAdapter mAdapter;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ImageActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        mAdapter = new ImageRecyclerAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            urlList.add(UrlConstant.getUrl());
        }

        mAdapter.setDataList(urlList, true);
    }

    private class ImageRecyclerAdapter extends AbstractCommonRecyclerAdapter<String> {
        @Override
        public int getItemRes() {
            return R.layout.item_image;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            ImageView imageView = holder.get(R.id.iv_square);

            String url = getItem(position);
            LogFileUtil.v("url = " + url + ",imageView = " + imageView);
            IApplication.getImageLoader().bindBitmap(url, imageView);
        }
    }
}
