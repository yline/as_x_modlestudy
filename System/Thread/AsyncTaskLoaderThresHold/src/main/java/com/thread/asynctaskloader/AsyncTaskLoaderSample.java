package com.thread.asynctaskloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yline on 2016/10/30.
 */
public class AsyncTaskLoaderSample extends AsyncTaskLoader<List<String>>
{
	private int mNumber;

	public AsyncTaskLoaderSample(Context context, int number)
	{
		super(context);
		LogFileUtil.v("AsyncTaskLoaderSample");
		this.mNumber = number;
	}

	@Override
	public List<String> loadInBackground()
	{
		LogFileUtil.v("loadInBackground ");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < mNumber; i++)
		{
			String s = "AsyncTaskLoaderSample -> " + i;
			list.add(s);
		}
		return list;
	}

	/*********************** 不是必须的 *************************/
	@Override
	protected void onStartLoading()
	{
		super.onStartLoading();
		LogFileUtil.v("onStartLoading ");
		forceLoad();
	}

	@Override
	protected void onForceLoad()
	{
		super.onForceLoad();
		LogFileUtil.v("onForceLoad");
	}

	@Override
	public void deliverResult(List<String> data)
	{
		super.deliverResult(data);
		LogFileUtil.v("deliverResult ");
	}

	@Override
	protected void onStopLoading()
	{
		super.onStopLoading();
		LogFileUtil.v("onStopLoading ");
	}

	@Override
	protected void onAbandon()
	{
		super.onAbandon();
		LogFileUtil.v("onAbandon ");
	}

	@Override
	protected void onReset()
	{
		super.onReset();
		LogFileUtil.v("onReset ");
	}

}
