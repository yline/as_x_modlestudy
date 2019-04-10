package com.multi.filter.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.yline.base.BaseReceiver;
import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

/**
 * Created by yline on 2016/10/25.
 */
public class TestReceiver extends BaseReceiver {
    public static final String ONE_1 = "test.one.one";
    private static final String ONE_2 = "test.one.two";

    public static final String TWO_1 = "test.two.one";
    private static final String TWO_2 = "test.two.two";
    private static final String TWO_3 = "test.two.three";

    public static final String THREE_1 = "test.three.one";
    private static final String THREE_2 = "test.three.two";

    public static final String FOUR_1 = "test.four.one";

    /**
     * 这里是用来测试,通过多次新建过滤器的方式,试试看是否会分层
     * 结果:不会分层
     */
    public static TestReceiver register(Context context) {
        TestReceiver testReceiver = new TestReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TestReceiver.ONE_1);
        intentFilter.addAction(TestReceiver.ONE_2);
        context.registerReceiver(testReceiver, intentFilter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(TestReceiver.TWO_1);
        intentFilter.addAction(TestReceiver.TWO_2);
        intentFilter.addAction(TestReceiver.TWO_3);
        context.registerReceiver(testReceiver, intentFilter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(TestReceiver.THREE_1);
        intentFilter.addAction(TestReceiver.THREE_2);
        context.registerReceiver(testReceiver, intentFilter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(TestReceiver.FOUR_1);
        context.registerReceiver(testReceiver, intentFilter);
        return testReceiver;
    }

    public static void unRegister(Context context, TestReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (null != intent) {
            String action = intent.getAction();
            if (null != action) {
                LogUtil.v("action = " + action);
            } else {
                LogUtil.v("testReceiver action is null");
            }
        } else {
            LogUtil.v("testReceiver intent is null");
        }
    }
}
