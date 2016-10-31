package com.mediaplay.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.mediaplay.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

import java.io.IOException;

public class MainActivity extends BaseAppCompatActivity
{
	private static final int CHOOSE_MUSIC_CODE = 0;

	private static final String HINE_JUMP_FAILED = "choose music failed";

	private static final String TAG = "MediaPlayer";

	private String musicPath;

	private MediaPlayer mediaPlayer;

	private Button btnPlay;

	private static final String STATE_PLAY = "暂停";

	private static final String STATE_STOP = "开始";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_choose).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_choose");
				openAudio(MainActivity.this, CHOOSE_MUSIC_CODE);
			}
		});

		btnPlay = (Button) findViewById(R.id.btn_play);
		btnPlay.setOnClickListener(new MediaPlayerListener());
		btnPlay.setText(STATE_STOP);
		findViewById(R.id.btn_reset).setOnClickListener(new MediaPlayerListener());
	}

	/**
	 * 只是在手机上可以查找到music
	 * @param requestCode 请求码
	 */
	public void openAudio(Activity activity, int requestCode)
	{
		Intent tempIntent = new Intent();
		tempIntent.setAction(Intent.ACTION_PICK);
		tempIntent.setType("audio/*");
		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(tempIntent, requestCode);
		}
		else
		{
			MainApplication.toast(HINE_JUMP_FAILED);
		}
	}

	/**
	 * 初始化播放器
	 * @param path
	 * @return
	 */
	private MediaPlayer initMediaPlayer(String path)
	{
		MediaPlayer mediaPlayer = null;
		if (TextUtils.isEmpty(path))
		{
			LogFileUtil.e(TAG, "initMediaPlayer failed, path is null");
			return mediaPlayer;
		}

		mediaPlayer = new MediaPlayer();
		try
		{
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			return mediaPlayer;
		}
		catch (IOException e)
		{
			LogFileUtil.e(TAG, "initMediaPlayer IOException", e);
			return mediaPlayer;
		}
	}

	private class MediaPlayerListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (null == mediaPlayer)
			{
				MainApplication.toast("文件不存在,请选择文件");
				return;
			}

			switch (v.getId())
			{
				case R.id.btn_play:
					if (mediaPlayer.isPlaying())
					{
						// 暂停播放
						mediaPlayer.pause();
						btnPlay.setText(STATE_STOP);
					}
					else
					{
						mediaPlayer.start();
						btnPlay.setText(STATE_PLAY);
					}
					break;
				case R.id.btn_reset:
					mediaPlayer.seekTo(0);
					break;
				default:
					break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		LogFileUtil.v("onActivityResult requestCode = " + resultCode + ",resultCode = " + resultCode);
		if (null != data)
		{
			if (RESULT_OK == resultCode)
			{
				switch (requestCode)
				{
					case CHOOSE_MUSIC_CODE:
						musicPath = data.getData().getPath();
						LogFileUtil.v(MainApplication.TAG, "AUDIO_PICK -> musicPath -> " + musicPath);
						mediaPlayer = initMediaPlayer(musicPath);
						break;
				}
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (null != mediaPlayer)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}
}
