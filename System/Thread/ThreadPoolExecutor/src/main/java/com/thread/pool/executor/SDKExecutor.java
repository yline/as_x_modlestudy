package com.thread.pool.executor;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 不支持插队(优先级)功能
 *
 * @author yline 2016/11/10 --> 13:48
 * @version 1.0.0
 */
public class SDKExecutor implements Executor {
    private static final int CORE_POOL_SIZE = 3; // 默认，核心池格式

    private static final int MAXIMUM_POOL_SIZE = 256; // 最大排队的线程池个数

    private static final int KEEP_ALIVE = 5; // 闲置线程存活时间

    private final ThreadPoolExecutor mThreadPoolExecutor;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "xTID#" + mCount.getAndIncrement());
        }
    };

    private static final Comparator<Runnable> FIFO_COMPARE = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = ((PriorityRunnable) lhs);
                PriorityRunnable rpr = ((PriorityRunnable) rhs);
                return lpr.priority.ordinal() - rpr.priority.ordinal();
            } else {
                return 0;
            }
        }
    };

    /**
     * 默认工作线程数3
     */
    public SDKExecutor() {
        this(CORE_POOL_SIZE);
    }

    /**
     * @param poolSize 工作线程数
     */
    public SDKExecutor(int poolSize) {
        BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue<>(MAXIMUM_POOL_SIZE, FIFO_COMPARE);
        // BlockingQueue<Runnable> mPoolWorkQueue = new ArrayBlockingQueue<>(MAXIMUM_POOL_SIZE);

        /**
         * poolSize 核心池	同时活动的线程
         * MAXIMUM_POOL_SIZE 最大池	线程池的上限
         * KEEP_ALIVE 存活时间  一个线程已经闲置的时间超过了存活时间，它将成为一个被回收的候选者
         * TimeUnit.SECONDS 存活时间单位
         * TimeUnit.mPoolWorkQueue 工作队列		持有等待执行的任务	-- 任务排队有3种基本方法：无限队列、有限队列和同步移交
         * sThreadFactory 创建新线程时,调用该方法
         */
        mThreadPoolExecutor = new ThreadPoolExecutor(poolSize, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, mPoolWorkQueue, sThreadFactory);
    }

    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    public boolean isBusy() {
        return mThreadPoolExecutor.getActiveCount() >= mThreadPoolExecutor.getCorePoolSize();
    }
}
