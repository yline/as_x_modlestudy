package com.dialog;

import android.os.Bundle;
import android.view.View;

import com.dialog.style.ButtonDialogUtil;
import com.dialog.style.CustomDialogUtil;
import com.dialog.style.ListDialogUtil;
import com.dialog.style.ProgressDialogUtil;
import com.view.dialog.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_buttondialog_two).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_buttondialog_two");

				ButtonDialogUtil.showDialogTwo(MainActivity.this, R.mipmap.ic_launcher, "标题", "提示信息", false, "确定", "取消");
			}
		});

		findViewById(R.id.btn_buttondialog_three).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_buttondialog_three");

				ButtonDialogUtil.showDialogThree(MainActivity.this, R.mipmap.ic_launcher, "标题", "提示信息", false, "one", "two", "three");
			}
		});

		findViewById(R.id.btn_listdialog_select).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_listdialog_select");

				String[] strs = {"list1", "list2", "list3", "list4"};
				ListDialogUtil.showDialogSelect(MainActivity.this, "标题", strs);
			}
		});

		findViewById(R.id.btn_listdialog_radio).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_listdialog_radio");

				String[] strs1 = {"list11", "list12", "list13", "list14"};
				ListDialogUtil.showDialogRadio(MainActivity.this, R.mipmap.ic_launcher, "单选标题", strs1, "确定", "取消");

			}
		});

		findViewById(R.id.btn_listdialog_check).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_listdialog_check");

				String[] strs2 = {"list21", "list22", "list23", "list24"};
				ListDialogUtil.showtDialogCheck(MainActivity.this, R.mipmap.ic_launcher, "多选标题", strs2, "确定", "取消");
			}
		});

		findViewById(R.id.btn_progressdialog_line).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_progressdialog_line");

				ProgressDialogUtil.showDialogLine(MainActivity.this, R.mipmap.ic_launcher, "进度条", true);
			}
		});

		findViewById(R.id.btn_progressdialog_round).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_progressdialog_round");

				ProgressDialogUtil.showDialogRound(MainActivity.this, R.mipmap.ic_launcher, "进度条", true);
			}
		});

		findViewById(R.id.btn_customdialog_hint).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_customdialog_hint");

				CustomDialogUtil.showDialogHint(MainActivity.this, "就是吐丝啊");
			}
		});

		findViewById(R.id.btn_customdialog_notitle).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_customdialog_notitle");

				CustomDialogUtil.showDialogNotitle(MainActivity.this, "message", true);
			}
		});

		findViewById(R.id.btn_customdialog_design).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_customdialog_design");

				CustomDialogUtil.showDialogDesign(MainActivity.this, R.mipmap.ic_launcher, "标题", true, "Ok", "Cancle");
			}
		});

	}

	@Override
	public void onBackPressed()
	{
		LogFileUtil.v(MainApplication.TAG, "onBackPressed"); // 若为false,并不会被执行
		super.onBackPressed();
	}

}
