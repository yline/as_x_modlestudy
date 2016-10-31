package com.tools.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.appother.tools.R;
import com.tools.SystemSkipTool;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import java.io.File;

/**
 * http://blog.csdn.net/wl455624651/article/details/7943252  各种跳转
 */
public class MainActivity extends BaseActivity
{
	private SystemSkipTool mSystemSkipTool;

	private static final int ALBUM_PICK = 1;

	private static final int ALBUM_PICK_ZOOM = 2;

	private final String backUri = Environment.getExternalStorageDirectory() + "/temp.jpg";

	private static final int AUDIO_PICK = 3;

	private static final int FILE_CHOOSE = 5;

	private static final int SETTING_WIFI = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSystemSkipTool = new SystemSkipTool();

		findViewById(R.id.btn_brower_web).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_brower_web");

				mSystemSkipTool.openBrower(MainActivity.this, "www.baidu.com");
			}
		});

		findViewById(R.id.btn_album_pick).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_album_pick");

				mSystemSkipTool.openAlbum(MainActivity.this, ALBUM_PICK);
			}
		});

		findViewById(R.id.btn_audio_pick).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_audio_pick");

				mSystemSkipTool.openAudio(MainActivity.this, AUDIO_PICK);
			}
		});

		findViewById(R.id.btn_file_choose).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_file_choose");

				mSystemSkipTool.openFile(MainActivity.this, FILE_CHOOSE);
			}
		});

		findViewById(R.id.btn_setting_wifi).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_setting_wifi");
				mSystemSkipTool.openSetting(MainActivity.this, SETTING_WIFI);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		LogFileUtil.v(MainApplication.TAG, "onActivityResult data is null ? -> " + (data == null));
		if (null != data)
		{
			switch (requestCode)
			{
				case ALBUM_PICK:
					LogFileUtil.v(MainApplication.TAG, "ALBUM_PICK -> " + data.getData());
					mSystemSkipTool.openAlbumZoom(MainActivity.this, data.getData(), Uri.fromFile(new File(backUri)), ALBUM_PICK_ZOOM);
					break;
				case ALBUM_PICK_ZOOM:
					LogFileUtil.v(MainApplication.TAG, "ALBUM_PICK_ZOOM -> " + backUri);
					break;
				case AUDIO_PICK:
					LogFileUtil.v(MainApplication.TAG, "AUDIO_PICK -> " + data.getData().getPath());
					break;
				case FILE_CHOOSE:
					LogFileUtil.v(MainApplication.TAG, "FILE_CHOOSE -> " + data.getExtras());
					break;
				case SETTING_WIFI:
					LogFileUtil.v(MainApplication.TAG, "SETTING_WIFI -> " + data.getExtras());
					break;
				default:
					LogFileUtil.e(MainApplication.TAG, "onActivityResult requestCode exception");
					break;
			}
		}
	}

}
