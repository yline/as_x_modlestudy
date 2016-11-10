package com.thread.pool;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 不支持插队(优先级)功能
 * @author yline 2016/11/10 --> 13:48
 * @version 1.0.0
 */
public class SDKExecutor implements Executor
{
	private static final int CORE_POOL_SIZE = 3;

	private static final int MAXIMUM_POOL_SIZE = 256;

	private static final int KEEP_ALIVE = 1;

	private final ThreadPoolExecutor mThreadPoolExecutor;

	private static final ThreadFactory sThreadFactory = new ThreadFactory()
	{
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable runnable)
		{
			return new Thread(runnable, "xTID#" + mCount.getAndIncrement());
		}
	};

	private static final Comparator<Runnable> FIFO_CMP = new Comparator<Runnable>()
	{
		@Override
		public int compare(Runnable lhs, Runnable rhs)
		{
			if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable)
			{
				PriorityRunnable lpr = ((PriorityRunnable) lhs);
				PriorityRunnable rpr = ((PriorityRunnable) rhs);
				int result = lpr.priority.ordinal() - rpr.priority.ordinal();
				return result == 0 ? (int) (lpr.SEQ - rpr.SEQ) : result;
			}
			else
			{
				return 0;
			}
		}
	};

	private static final Comparator<Runnable> FILO_CMP = new Comparator<Runnable>()
	{
		@Override
		public int compare(Runnable lhs, Runnable rhs)
		{
			if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable)
			{
				PriorityRunnable lpr = ((PriorityRunnable) lhs);
				PriorityRunnable rpr = ((PriorityRunnable) rhs);
				int result = lpr.priority.ordinal() - rpr.priority.ordinal();
				return result == 0 ? (int) (rpr.SEQ - lpr.SEQ) : result;
			}
			else
			{
				return 0;
			}
		}
	};

	/**
	 * 默认工作线程数3
	 */
	public SDKExecutor()
	{
		this(CORE_POOL_SIZE, false);
	}

	/**
	 * 默认工作线程数3
	 * @param fifo 优先级相同时, 等待队列的是否优先执行先加入的任务.
	 */
	public SDKExecutor(boolean fifo)
	{
		this(CORE_POOL_SIZE, fifo);
	}

	/**
	 * @param poolSize 工作线程数
	 * @param fifo     优先级相同时, 等待队列的是否优先执行先加入的任务.
	 */
	public SDKExecutor(int poolSize, boolean fifo)
	{
		BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue<Runnable>(MAXIMUM_POOL_SIZE, fifo ? FIFO_CMP : FILO_CMP);
		// BlockingQueue<Runnable> mPoolWorkQueue = new ArrayBlockingQueue<>(MAXIMUM_POOL_SIZE);
		mThreadPoolExecutor = new ThreadPoolExecutor(poolSize, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, mPoolWorkQueue, sThreadFactory);
	}

	public boolean isBusy()
	{
		return mThreadPoolExecutor.getActiveCount() >= mThreadPoolExecutor.getCorePoolSize();
	}

	@Override
	public void execute(Runnable runnable)
	{
		mThreadPoolExecutor.execute(runnable);
	}
}
