package com.viewgroup.swipeback;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * 主Style
 * @author yline 2017/10/26 -- 14:12
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("界面跳转", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.launcher(MainActivity.this);
            }
        });
    }
}
