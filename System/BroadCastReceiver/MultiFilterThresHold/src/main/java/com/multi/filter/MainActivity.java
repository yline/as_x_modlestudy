package com.multi.filter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.multi.filter.receiver.TestReceiver;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

/**
 * 不会分层
 */
public class MainActivity extends BaseAppCompatActivity {
    private TestReceiver mTestReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestReceiver = TestReceiver.register(MainActivity.this);

        findViewById(R.id.btn_send_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogFileUtil.v("btn_send_one");
                sendBroadcast(new Intent(TestReceiver.ONE_1));
            }
        });
        findViewById(R.id.btn_send_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogFileUtil.v("btn_send_two");
                sendBroadcast(new Intent(TestReceiver.TWO_1));
            }
        });
        findViewById(R.id.btn_send_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogFileUtil.v("btn_send_three");
                sendBroadcast(new Intent(TestReceiver.THREE_1));
            }
        });
        findViewById(R.id.btn_send_four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogFileUtil.v("btn_send_four");
                sendBroadcast(new Intent(TestReceiver.FOUR_1));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TestReceiver.unRegister(this, mTestReceiver);
    }
}
