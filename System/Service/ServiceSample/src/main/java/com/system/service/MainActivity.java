package com.system.service;

import android.os.Bundle;
import android.os.Process;
import android.view.View;

import com.system.service.diff.DiffServiceActivity;
import com.system.service.intent.TestIntentService;
import com.system.service.same.SameServiceActivity;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        LogUtil.v("testStart pid = " + Process.myPid() + ", tid = " + Process.myTid());

        addButton("Intent", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestIntentService.serviceStart(MainActivity.this);
            }
        });

        addButton("同一进程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SameServiceActivity.launch(MainActivity.this);
            }
        });

        addButton("不同进程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiffServiceActivity.launch(MainActivity.this);
            }
        });
    }
}
