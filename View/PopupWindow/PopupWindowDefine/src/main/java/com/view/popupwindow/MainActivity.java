package com.view.popupwindow;

import android.os.Bundle;
import android.view.View;

import com.view.popupwindow.activity.DeleteMenuActivity;
import com.view.popupwindow.activity.DropMenuActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("长按删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMenuActivity.launcher(MainActivity.this);
            }
        });

        addButton("下拉菜单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropMenuActivity.launcher(MainActivity.this);
            }
        });
    }
}
