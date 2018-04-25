package com.thread.pool.activity;

import android.os.Bundle;
import android.view.View;

import com.thread.pool.executor.Executor;
import com.thread.pool.executor.PriorityRunnable;
import com.thread.pool.executor.SDKExecutor;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;

/**
 * 测试代码
 *
 * @author yline 2018/4/25 -- 19:44
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000000; i++) {
                    if (i % 10000000 == 0) {
                        LogFileUtil.v("Thread = " + Thread.currentThread().getId() + ", i = " + i);
                    }
                }
            }
        };

        addButton("execute", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogFileUtil.v(IApplication.TAG, "execute");
                // 调用两次就会执行两次,然后,点击Button两次,就会出现线程池的排队效果
                IApplication.start(runnable, null);
                IApplication.start(runnable, null);
            }
        });

        addButton("execute Diff", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogFileUtil.v(IApplication.TAG, "execute");
                // 调用两次就会执行两次,然后,点击Button两次,就会出现线程池的排队效果
                Executor executor = new SDKExecutor(1);

                executor.execute(new PriorityRunnable(runnable, PriorityRunnable.Priority.DEFAULT));
                executor.execute(new PriorityRunnable(runnable, PriorityRunnable.Priority.UI_LOW));
            }
        });
    }
}
