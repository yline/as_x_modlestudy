package com.viewpager.tab;

import android.os.Bundle;
import android.view.View;

import com.viewpager.tab.TabActivity.TabActivity;
import com.viewpager.tab.TabLayoutActivity.TabLayoutActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Tab", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.launcher(MainActivity.this);
            }
        });

        addButton("TabLayout", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayoutActivity.launcher(MainActivity.this);
            }
        });
    }
}
