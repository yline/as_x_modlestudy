package com.utils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.appother.utils.R;
import com.utils.KeyBoardUtil;
import com.utils.MD5Util;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;
import com.yline.utils.AppUtil;
import com.yline.utils.SDCardUtil;
import com.yline.utils.TimeConvertUtil;
import com.yline.utils.TimeStampUtil;

public class MainActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_app_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_app_util");

				String appName = AppUtil.getAppName(MainApplication.getApplication());
				String versionName = AppUtil.getVersionName(MainApplication.getApplication());
				LogFileUtil.i(MainApplication.TAG, "appName = " + appName + ",versionName = " + versionName);

				MainApplication.toast("appName = " + appName + ",versionName = " + versionName);
			}
		});

		// 软键盘
		final EditText etKeyBoard = (EditText) findViewById(R.id.et_keyboard_util);
		findViewById(R.id.btn_keyboard_open).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_keyboard_open");

				KeyBoardUtil.openKeybord(MainActivity.this, etKeyBoard);

			}
		});
		findViewById(R.id.btn_keyboard_close).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_keyboard_close");

				KeyBoardUtil.closeKeybord(MainActivity.this, etKeyBoard);
			}
		});

		findViewById(R.id.btn_md5_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_md5_util");

				byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
				String hexByBytes = MD5Util.toHexString(bytes);
				String md5ByString = MD5Util.md5(hexByBytes);
				LogFileUtil.v(MainApplication.TAG, "hexByBytes = " + hexByBytes + ",md5ByString = " + md5ByString);
			}
		});

		findViewById(R.id.btn_sdcard_util).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_sdcard");

				LogFileUtil.i(MainApplication.TAG, "SDCardUtil.isSDCardEnable() = " + SDCardUtil.isSDCardEnable());

				LogFileUtil.i(MainApplication.TAG, "SDCardUtil.getRootDirectoryPath() = " + SDCardUtil.getRootDirectoryPath());
				LogFileUtil.i(MainApplication.TAG, "SDCardUtil.getSDCardPath() = " + SDCardUtil.getSDCardPath());

				LogFileUtil.i(MainApplication.TAG, "SDCardUtil.getSDCardAvailableSize() = " + SDCardUtil.getSDCardAvailableSize() * 1.0f / (1024 * 1024 * 1024));
				LogFileUtil.i(MainApplication.TAG, "SDCardUtil.getSDCardBlockSize() = " + SDCardUtil.getSDCardBlockSize() * 1.0f / (1024 * 1024 * 1024));
			}
		});

		findViewById(R.id.btn_timeconvert_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_timeconvert_util");

				String formateMinute = TimeConvertUtil.ms2FormatMinute(12223000);
				LogFileUtil.v(MainApplication.TAG, "formateMinute = " + formateMinute);
			}
		});

		findViewById(R.id.btn_timestamp_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_timestamp_util");

				long currentStamp = TimeStampUtil.getCurrentStamp();
				String timeStandard = TimeStampUtil.getTimeStandard(currentStamp);
				LogFileUtil.v(MainApplication.TAG, "currentStamp = " + currentStamp + ",timeStandard = " + timeStandard);

				int year = TimeStampUtil.getYear(currentStamp);
				int month = TimeStampUtil.getMonth(currentStamp);
				int day = TimeStampUtil.getDay(currentStamp);
				int hour = TimeStampUtil.getHour(currentStamp);
				int minute = TimeStampUtil.getMinute(currentStamp);
				int second = TimeStampUtil.getSecond(currentStamp);
				int dayOfWeekEnglish = TimeStampUtil.getDayOfWeekEnglish(currentStamp);
				int dayOfWeek = TimeStampUtil.getDayOfWeek(currentStamp);
				LogFileUtil.v(MainApplication.TAG, "year = " + year + ",month = " + month + "day = " + day + ",hour = " + hour + ",minute = " + minute + ",second = " + second + ",dayOfWeekEnglish = " + dayOfWeekEnglish + ",dayOfWeek = " + dayOfWeek);

				long diffStamp = TimeStampUtil.getDiffStamp(currentStamp);
				boolean isStampTimeOut = TimeStampUtil.isStampTimeOut(currentStamp, 20);
				LogFileUtil.v(MainApplication.TAG, "diffStamp = " + diffStamp + ",isStampTimeOut = " + isStampTimeOut);
			}
		});

		findViewById(R.id.btn_toast_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String content = "多的是你不知道的事";
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_toast_util content = " + content);

				MainApplication.toast(content);
			}
		});

		findViewById(R.id.btn_unkown_util).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_unkown_util");
			}
		});
	}
}
