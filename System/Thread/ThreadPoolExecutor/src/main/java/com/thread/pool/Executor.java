package com.thread.pool;

import java.util.concurrent.RejectedExecutionException;

/**
 * Created by yline on 2016/11/10.
 */
public interface Executor
{
	/**
	 * Executes the given command at some time in the future.  The command
	 * may execute in a new thread, in a pooled thread, or in the calling
	 * thread, at the discretion of the {@code Executor} implementation.
	 * @param command the runnable task
	 * @throws RejectedExecutionException if this task cannot be
	 *                                    accepted for execution
	 * @throws NullPointerException       if command is null
	 */
	void execute(Runnable command);
}
