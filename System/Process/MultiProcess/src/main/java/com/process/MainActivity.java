package com.process;

import android.os.Bundle;
import android.view.View;

import com.process.activity.CoreProcessActivity;
import com.process.activity.ImageProcessActivity;
import com.process.activity.PushProcessActivity;
import com.yline.test.BaseTestActivity;

/**
 * 主进程
 *
 * @author yline 2018/4/19 -- 15:26
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton(":core", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoreProcessActivity.launcher(MainActivity.this);
            }
        });

        addButton(":image", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageProcessActivity.launcher(MainActivity.this);
            }
        });

        addButton(":push", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushProcessActivity.launcher(MainActivity.this);
            }
        });
    }
}
