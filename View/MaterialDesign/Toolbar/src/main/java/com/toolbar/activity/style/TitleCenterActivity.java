package com.toolbar.activity.style;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.toolbar.R;
import com.yline.base.BaseAppCompatActivity;

public class TitleCenterActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_center);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_title_center);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // toolbar.setLogo(R.mipmap.ic_launcher); // 图标
        // toolbar.setTitle("MainBar"); // 主标题
        // toolbar.setSubtitle("SubBar"); // 副标题
        // toolbar.setNavigationIcon(R.mipmap.ic_launcher); // 左边可点击按钮
        // toolbar.setOnMenuItemClickListener();
    }

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, TitleCenterActivity.class));
    }
}
