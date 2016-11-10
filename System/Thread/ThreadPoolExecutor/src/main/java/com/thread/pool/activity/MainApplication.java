package com.thread.pool.activity;

import com.thread.pool.Executor;
import com.thread.pool.PriorityRunnable;
import com.thread.pool.SDKExecutor;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/11/10.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "ThreadPoolExecutor";

	private static final Executor executor = new SDKExecutor();

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}

	/**
	 * 运行一个线程,并且放入线程池中
	 * @param runnable
	 * @param priority 优先级
	 */
	public static void start(Runnable runnable, PriorityRunnable.Priority priority)
	{
		executor.execute(new PriorityRunnable(runnable, priority));
	}
}
