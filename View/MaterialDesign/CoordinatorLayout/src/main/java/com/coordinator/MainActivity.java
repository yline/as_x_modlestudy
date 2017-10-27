package com.coordinator;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("RecyclerView", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerActivity.actionStart(MainActivity.this);
            }
        });

        addButton("ScrollView", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addButton("TabLayout", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
