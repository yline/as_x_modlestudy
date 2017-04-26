package com.tools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appother.tools.R;
import com.tools.IntentHelper;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	private static final int ALBUM_PICK = 1;

	private static final int ALBUM_PICK_ZOOM = 2;

	private final String fileName = "temp.jpg";

	private static final int AUDIO_PICK = 3;

	private static final int FILE_CHOOSE = 5;

	private static final int SETTING_WIFI = 7;

	private static final int CAMERA = 8;

	private IntentHelper helper = IntentHelper.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 打开相册、图片裁剪
		findViewById(R.id.btn_album_pick).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_album_pick");

				helper.openAlbum(MainActivity.this, ALBUM_PICK);
			}
		});

		// 打开音乐播放器
		findViewById(R.id.btn_audio_pick).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_audio_pick");

				helper.openAudio(MainActivity.this, AUDIO_PICK);
			}
		});

		// 打开浏览器
		findViewById(R.id.btn_brower_web).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_brower_web");

				helper.openBrower(MainActivity.this, "www.baidu.com");
			}
		});

		// 打开照相机
		findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_camera");

				helper.openCamera(MainActivity.this, fileName, CAMERA);
			}
		});

		// 打开联系人界面
		findViewById(R.id.btn_contact).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_contact");

				helper.openContact(MainActivity.this);
			}
		});

		// 打开文件管理器
		findViewById(R.id.btn_file_choose).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_file_choose");

				helper.openFile(MainActivity.this, FILE_CHOOSE);
			}
		});

		// 打开录音器
		findViewById(R.id.btn_record).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_record");

				helper.openRecord(MainActivity.this);
			}
		});

		// 打开设置界面的Wifi界面
		findViewById(R.id.btn_setting_wifi).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_setting_wifi");
				helper.openSettingWifi(MainActivity.this, SETTING_WIFI);
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
					helper.openPictureZoom(MainActivity.this, data.getData(), fileName, ALBUM_PICK_ZOOM);
					break;
				case ALBUM_PICK_ZOOM:
					LogFileUtil.v(MainApplication.TAG, "ALBUM_PICK_ZOOM -> " + fileName);
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
				case CAMERA:
					LogFileUtil.v(MainApplication.TAG, "CAMERA -> " + data.getExtras());
					break;
				default:
					LogFileUtil.e(MainApplication.TAG, "onActivityResult requestCode exception");
					break;
			}
		}
	}

}
