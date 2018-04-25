package com.thread.pool.activity;

import com.thread.pool.executor.Executor;
import com.thread.pool.executor.PriorityRunnable;
import com.thread.pool.executor.SDKExecutor;
import com.yline.application.BaseApplication;

/**
 * 入口
 *
 * @author yline 2018/4/25 -- 19:43
 * @version 1.0.0
 */
public class IApplication extends BaseApplication {
    private static final Executor executor = new SDKExecutor();

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
