package com.thread.pool.executor;

/**
 * 具有优先级的
 *
 * @author yline 2018/4/25 -- 19:45
 * @version 1.0.0
 */
public class PriorityRunnable implements Runnable {
    public final Priority priority;

    private final Runnable runnable;

    public PriorityRunnable(Runnable runnable, Priority priority) {
        this.priority = priority == null ? Priority.DEFAULT : priority;
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        this.runnable.run();
    }

    /**
     * 任务的优先级
     *
     * @author yline 2016/11/10 --> 15:13
     * @version 1.0.0
     */
    public enum Priority {
        UI_TOP, UI_NORMAL, UI_LOW, DEFAULT, BG_TOP, BG_NORMAL, BG_LOW;
    }
}