package com.local;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

/**
 * 主要测试
 * 解决线程安全原理：私有变量不存在线程安全问题
 *
 * @author yline 2018/5/4 -- 16:54
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {
    static final ThreadLocal<Long> longLocal = new ThreadLocal<>();
    static final ThreadLocal<String> stringLocal = new ThreadLocal<>();

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("testThreadLocal", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testThreadLocal("newThreadA", "newThreadB");
            }
        });
    }

    // 被调用的 入口
    public void testThreadLocal(String threadNameA, String threadNameB) {
        set();
        new Thread(new Runnable() {
            @Override
            public void run() {
                set();
                get();
            }
        }, threadNameA).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                set();
                get();
            }
        }, threadNameB).start();
        get();
    }

    private void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    private void get() {
        long longNum = longLocal.get();
        String strName = stringLocal.get();

        LogUtil.v("longNum = " + longNum + ", strName = " + strName);
    }
}
