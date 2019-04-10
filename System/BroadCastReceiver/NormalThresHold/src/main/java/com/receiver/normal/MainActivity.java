package com.receiver.normal;

import android.os.Bundle;
import android.view.View;

import com.receiver.normal.receiver.UIRefreshReceiver;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import static com.receiver.normal.receiver.UIRefreshReceiver.UI_MAIN_HOME;

public class MainActivity extends BaseTestActivity {
    private UIRefreshReceiver mReceiver;

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        mReceiver = UIRefreshReceiver.register(this);
        mReceiver.setOnRefreshListener(new UIRefreshReceiver.OnRefreshListener() {
            @Override
            public void onReceiver(int from, int to) {
                LogUtil.v("from = " + from + ", to = " + to);
            }
        });

        addButton("发送广播", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIRefreshReceiver.sendAction(MainActivity.this, UI_MAIN_HOME, UI_MAIN_HOME);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIRefreshReceiver.unRegister(this, mReceiver);
    }
}
