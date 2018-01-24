package com.view.recycler.choice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.decoration.CommonLinearDecoration;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

public class MultipleChoiceActivity extends BaseAppCompatActivity {
    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, MultipleChoiceActivity.class));
    }

    private AbstractCommonRecyclerAdapter<String> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CommonLinearDecoration(this));

        recyclerAdapter = new AbstractCommonRecyclerAdapter<String>() {
            private boolean[] isSelected;

            @Override
            public void setDataList(List<String> strings, boolean isNotify) {
                super.setDataList(strings, isNotify);
                isSelected = new boolean[strings.size()];
            }

            @Override
            public int getItemRes() {
                return R.layout.item_recycler;
            }

            @Override
            public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
                viewHolder.setText(R.id.tv_item, getItem(position));

                viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isSelected[position] = !isSelected[position];
                        notifyItemChanged(position);
                    }
                });

                updateItemState(viewHolder, position);
            }

            private void updateItemState(RecyclerViewHolder viewHolder, int position) {
                viewHolder.getItemView().setSelected(isSelected[position]);
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setDataList(StrConstant.getListRandom(20), true);
    }
}
