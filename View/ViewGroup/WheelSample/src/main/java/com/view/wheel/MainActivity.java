package com.view.wheel;

import android.os.Bundle;
import android.view.View;

import com.view.wheel.area.AreaActivity;
import com.view.wheel.sample.SingleActivity;
import com.view.wheel.time.TimeActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Single", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleActivity.launcher(MainActivity.this);
            }
        });

        addButton("Time", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeActivity.launcher(MainActivity.this);
            }
        });

        addButton("Area", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AreaActivity.launcher(MainActivity.this);
            }
        });
    }
}
