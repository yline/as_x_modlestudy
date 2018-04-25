package com.thread.pool.executor;

import java.util.concurrent.RejectedExecutionException;

/**
 * 线程池的接口
 *
 * @author yline 2018/4/25 -- 20:16
 * @version 1.0.0
 */
public interface Executor {
    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     *                                    accepted for execution
     * @throws NullPointerException       if command is null
     */
    void execute(Runnable command);
}
