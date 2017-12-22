package com.coordinator;

import android.os.Bundle;
import android.view.View;

import com.coordinator.collapsingtoolbar.CollapsingToolbarActivity;
import com.coordinator.floatingaction.FloatingActionActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("FloatingActionButton", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingActionActivity.actionStart(MainActivity.this);
            }
        });

        addButton("CollapsingToolbarLayout", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollapsingToolbarActivity.launcher(MainActivity.this);
            }
        });
    }
}
