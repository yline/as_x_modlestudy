package com.udp.demo.activity;

import com.udp.demo.executor.Executor;
import com.udp.demo.executor.PriorityRunnable;
import com.udp.demo.executor.SDKExecutor;
import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication {
    private static final Executor executor = new SDKExecutor();

    public static final String TAG = "UDP&Instance";

    /**
     * 运行一个线程,并且放入线程池中
     *
     * @param runnable
     * @param priority 优先级
     */
    public static void start(Runnable runnable, PriorityRunnable.Priority priority) {
        executor.execute(new PriorityRunnable(runnable, priority));
    }
}
