package com.system.app.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.system.app.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicHelper
{
	private static final String TAG = "MusicHelper";

	private List<MusicBean> mMusicBeanList;

	private onScanCallback scanCallback;

	private MusicHelper()
	{

	}

	public static MusicHelper getInstance()
	{
		return MusicHelperHolder.instance;
	}

	private static class MusicHelperHolder
	{
		private static MusicHelper instance = new MusicHelper();
	}

	/**
	 * 测试该帮助类
	 */
	public void test(Context context)
	{
		// 3652 条数据,不打印日志大概3s扫描结束.
		MusicHelper.getInstance().scanMusicFromLocal(context, new onScanCallback()
		{
			@Override
			public void onStart()
			{

			}

			@Override
			public void onFinish(List<MusicBean> musicBeanList, int size)
			{
				MainApplication.toast("scan over size = " + size);
				LogFileUtil.v(TAG, "scan over size = " + size);
				/*
				for (MusicBean musicBean : musicBeanList)
				{
					LogFileUtil.v(TAG, musicBean.toString());
				}
				*/
			}
		});
	}

	public synchronized void scanMusicFromLocal(Context context, onScanCallback callback)
	{
		this.scanCallback = callback;

		ScanTask scanTask = new ScanTask(context);
		scanTask.execute();
	}

	private class ScanTask extends AsyncTask<Void, Void, Integer>
	{
		private Context sContext;

		public ScanTask(Context context)
		{
			this.sContext = context;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			LogFileUtil.v(TAG, "ScanTask onPreExecute run.");
			mMusicBeanList = new ArrayList<MusicBean>();

			if (null != scanCallback)
			{
				scanCallback.onStart();
			}
		}

		@Override
		protected Integer doInBackground(Void... params)
		{
			ContentResolver resolver = sContext.getContentResolver();

			// content://media/external/audio/media
			Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			Cursor cursor = resolver.query(uri, MusicBean.getProjection(), null, null, null);
			saveScanResult(cursor);
			return mMusicBeanList.size();
		}

		@Override
		protected void onPostExecute(Integer size)
		{
			LogFileUtil.v(TAG, "ScanTask onPostExecute size = " + size);

			if (null != scanCallback)
			{
				scanCallback.onFinish(mMusicBeanList, size);
			}
		}
	}

	/**
	 * 设置扫描结果
	 * @param cursor
	 */
	private void saveScanResult(Cursor cursor)
	{
		if (null == cursor)
		{
			LogFileUtil.v(TAG, "cursor is null");
			return;
		}

		boolean hasData = cursor.moveToFirst();
		MusicBean musicBean;

		LogFileUtil.v(TAG, "cursor hasData = " + hasData);
		while (hasData)
		{
			musicBean = new MusicBean(cursor);

			hasData = cursor.moveToNext();
			mMusicBeanList.add(musicBean);
		}
	}

	public interface onScanCallback
	{
		void onStart();

		/**
		 * 结束扫描
		 * @param musicBeanList 扫描结果数据
		 * @param size          扫描结果数据大小
		 */
		void onFinish(List<MusicBean> musicBeanList, int size);
	}

}
