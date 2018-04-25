package com.system.handler.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.system.handler.TestThread;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        final TextView tvTest = addTextView("");

        final TestThread testThread = new TestThread();
        testThread.setOnThreadCallback(new TestThread.OnThreadCallback() {
            @Override
            public void onResult(int number) {
                tvTest.setText("number is " + number);
            }
        });

        addButton("testThread", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!testThread.isAlive()) {
                    testThread.start();
                }
            }
        });
    }
}
