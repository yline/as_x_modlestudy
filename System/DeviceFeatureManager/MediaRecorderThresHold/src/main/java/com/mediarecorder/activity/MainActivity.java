package com.mediarecorder.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mediarecorder.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.io.IOException;

public class MainActivity extends BaseAppCompatActivity
{
	private static final String TAG = "MediaRecorderThresHold";

	private MediaRecorder mRecorder;

	private TextView btnRecorder;

	private static final int IS_PRESSING = 0;

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == IS_PRESSING && btnRecorder.isPressed())
			{
				LogFileUtil.v("Amplitude = " + getAmplitude());
				mHandler.sendEmptyMessageDelayed(IS_PRESSING, 200);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRecorder = (TextView) findViewById(R.id.btn_recorder);
		/** 事件中,必须加上ACTION_MOVE判断。否则,调用完ACTION_DOWN会立即调用一次ACTION_UP */
		btnRecorder.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						LogFileUtil.v("setOnTouchListener ACTION_DOWN");
						startRecord("voice" + System.currentTimeMillis() + ".mp3");
						mHandler.sendEmptyMessageDelayed(IS_PRESSING, 200);
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						LogFileUtil.v("setOnTouchListener ACTION_UP");
						stopRecord();
						break;
				}
				return false;
			}
		});
	}

	/**
	 * 声音 dB  0 ~ 90.3 dB
	 * @return 0 ~ 90.3 的数值
	 */
	public int getAmplitude()
	{
		if (null != mRecorder)
		{
			double x = mRecorder.getMaxAmplitude();
			if (x != 0)
			{
				return (int) (20 * Math.log10(x));   // 以10为底
			}
		}
		return -1;
	}

	/**
	 * 初始录音 开始
	 * @param name 文件名
	 */
	private boolean startRecord(String name)
	{
		String path = MainApplication.getProjectFilePath();
		if (null != path)
		{
			path += ("Recorder" + File.separator);
			if (TextUtils.isEmpty(name))
			{
				LogFileUtil.e(TAG, "media recorder name is null, recorder failed");
				return false;
			}

			if (null == FileUtil.createFileDir(path))
			{
				LogFileUtil.e(TAG, "media recorder createFileDir error, recorder failed");
				return false;
			}

			if (null == mRecorder)
			{
				mRecorder = new MediaRecorder();
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // 格式
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 格式
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 格式
				mRecorder.setOutputFile(path + name);
				try
				{
					mRecorder.prepare();
					mRecorder.start();
					return true;
				}
				catch (IllegalStateException e)
				{
					LogFileUtil.e(TAG, "startRecorder IllegalStateException", e);
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "startRecorder IOException", e);
				}
			}

			return false;
		}
		else
		{
			LogFileUtil.e(TAG, "SDCard not support, media recorder failed");
			return true;
		}
	}

	/**
	 * 录音 结束
	 */
	private void stopRecord()
	{
		if (null != mRecorder)
		{
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}
}
