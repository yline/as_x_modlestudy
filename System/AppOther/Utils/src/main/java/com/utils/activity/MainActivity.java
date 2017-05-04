package com.utils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.utils.KeyBoardUtil;
import com.utils.MD5Util;
import com.utils.NetworkUtil;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity
{
	@Override
	protected void testStart(Bundle savedInstanceState)
	{
		// KeyBoardUtil
		final EditText etKeyBoard = addEditNumber("软键盘输入框");
		addButton("KeyBoardUtil open", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_keyboard_open");

				KeyBoardUtil.openKeybord(MainActivity.this, etKeyBoard);
			}
		});
		addButton("KeyBoardUtil close", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_keyboard_close");

				KeyBoardUtil.closeKeybord(MainActivity.this, etKeyBoard);
			}
		});

		// MD5
		addButton("MD5", new View.OnClickListener()
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

		// NetworkUtil
		addButton("NetworkUtil", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NetworkUtil.NetType netType = NetworkUtil.getNetType(MainActivity.this);
				LogFileUtil.v(MainApplication.TAG, "netType = " + netType);
			}
		});
	}
}
