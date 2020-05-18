package com.yline.simple;

import android.os.Bundle;
import android.view.View;

import com.yline.simple.button.ButtonActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("button", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonActivity.launch(MainActivity.this);
            }
        });
    }
}
