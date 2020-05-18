package com.coordinator;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("CollapsingToolbarLayout", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollapsingToolbarActivity.launcher(MainActivity.this);
            }
        });
    }
}
