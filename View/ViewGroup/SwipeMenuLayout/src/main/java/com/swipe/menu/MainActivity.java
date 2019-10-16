package com.swipe.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.swipe.menu.view.EasySwipeMenuLayout;
import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.test.StrConstant;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

public class MainActivity extends BaseActivity {
    private MainRecyclerAdapter mRecyclerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        initData();
    }
    
    private void initView() {
        mRecyclerAdapter = new MainRecyclerAdapter();
        
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter);
        
        initViewClick();
    }
    
    private void initViewClick() {
    
    }
    
    private void initData() {
        mRecyclerAdapter.setDataList(StrConstant.getListRandom(30), true);
    }
    
    private static class MainRecyclerAdapter extends AbstractRecyclerAdapter<String> {
        @Override
        public int getItemRes() {
            return R.layout.item_main;
        }
        
        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
            final String value = get(i);
            
            String content = String.format("%s - %s - %s", value, i, value);
            viewHolder.setText(R.id.item_main_content_text, content);
            
            // 点击事件处理
            viewHolder.setOnClickListener(R.id.item_main_content, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.v("content value = " + value + ", state = " + EasySwipeMenuLayout.getState());
                    if (EasySwipeMenuLayout.getState() == EasySwipeMenuLayout.State.RIGHT_OPEN) {
                        EasySwipeMenuLayout.resetState();
                        return;
                    }
                    
                    SDKManager.toast(value + " - 被点击了！！！");
                }
            });
            
            viewHolder.setOnClickListener(R.id.item_main_right_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.v("delete value = " + value + ", state = " + EasySwipeMenuLayout.getState());
                    EasySwipeMenuLayout.resetState();
                }
            });
            
            viewHolder.setOnClickListener(R.id.item_main_right_collect, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.v("collect value = " + value + ", state = " + EasySwipeMenuLayout.getState());
                    EasySwipeMenuLayout.resetState();
                }
            });
        }
    }
}
