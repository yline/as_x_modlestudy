package com.view.popupwindow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;

import com.view.popupwindow.R;
import com.view.popupwindow.widget.DeleteMenuWidget;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.test.StrConstant;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.recycler.simple.SimpleCommonRecyclerAdapter;

public class DeleteMenuActivity extends BaseAppCompatActivity {
    private SimpleCommonRecyclerAdapter recyclerAdapter;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, DeleteMenuActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_menu);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new SimpleCommonRecyclerAdapter() {
            @Override
            public void onBindViewHolder(final RecyclerViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);

                DeleteMenuWidget widgetDeleteMenu = new DeleteMenuWidget(DeleteMenuActivity.this);
                widgetDeleteMenu.setOnWidgetListener(new DeleteMenuWidget.OnWidgetListener() {
                    @Override
                    public void onOptionSelected(View view, int position, String content) {
                        LogFileUtil.v("onOptionSelected position = " + position + ", content = " + content);
                    }
                });
                widgetDeleteMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        LogFileUtil.v("setOnDismissListener");
                    }
                });
                widgetDeleteMenu.showAtLocation(StrConstant.getListThree(3), viewHolder.getItemView());
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setDataList(StrConstant.getListRandom(30), true);
    }
}
