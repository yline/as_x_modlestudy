package com.yline.plugins;

import android.os.Bundle;
import android.view.View;

import com.yline.plugins.lifecycle.LifecycleActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        LifecycleActivity.launch(MainActivity.this);
        finish();
    }
}
