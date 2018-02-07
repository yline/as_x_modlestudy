package com.view.popupwindow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.view.popupwindow.activity.DeleteMenuActivity;
import com.view.popupwindow.activity.DropMenuActivity;
import com.view.popupwindow.view.TopMenuWidget;
import com.yline.test.BaseTestActivity;
import com.yline.test.StrConstant;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("长按删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMenuActivity.launcher(MainActivity.this);
            }
        });

        final Button secondBtn = addButton("下拉菜单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropMenuActivity.launcher(MainActivity.this);
            }
        });

        addButton("顶部弹出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopMenuWidget menuWidget = new TopMenuWidget(MainActivity.this);

                StringBuilder stringBuilder = new StringBuilder();
                for (String str : StrConstant.getListRandom(10)) {
                    stringBuilder.append(str);
                    stringBuilder.append('\n');
                }

                menuWidget.showAsDropDown(secondBtn, stringBuilder.toString());
            }
        });
    }
}
