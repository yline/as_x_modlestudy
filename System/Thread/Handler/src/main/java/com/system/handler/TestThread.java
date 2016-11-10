package com.system.handler;

import android.os.Handler;
import android.os.Message;

import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/11/10.
 */
public class TestThread extends Thread
{
	public static final int CODE_RUN_I = 0;

	private ThreadCallback callback;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (null != callback)
			{
				callback.onResult(msg.what, msg.arg1);
			}
		}
	};

	@Override
	public void run()
	{
		super.run();
		for (int i = 0; i < 1000000000; i++)
		{
			if (i % 100000000 == 0)
			{
				LogFileUtil.v("TestThread i = " + i);
				handler.obtainMessage(CODE_RUN_I, i, -1).sendToTarget();
			}
		}
	}

	public void setThreadCallback(ThreadCallback callback)
	{
		this.callback = callback;
	}

	public interface ThreadCallback
	{
		void onResult(int what, int arg1);
	}
}
