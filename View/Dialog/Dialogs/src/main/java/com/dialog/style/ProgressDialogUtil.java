package com.dialog.style;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dialog.MainApplication;
import com.yline.log.LogFileUtil;

public class ProgressDialogUtil
{
	private static ProgressDialog roundProgressDialog;

	/**
	 * 进度环
	 */
	public static void showDialogRound(Context context, int iconId, String title, boolean cancelable)
	{
		roundProgressDialog = new ProgressDialog(context);
		roundProgressDialog.setIcon(iconId);
		roundProgressDialog.setTitle(title);
		roundProgressDialog.setCancelable(cancelable);
		roundProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				LogFileUtil.v(MainApplication.TAG, "ProgressDialogUtil roundProgressDialog onDismiss");
			}
		});
		roundProgressDialog.show();
	}

	private static ProgressDialog lineProgressDialog;

	/**
	 * 进度条,这里调成了 加载完成自动结束的dialog
	 */
	public static void showDialogLine(Context context, int iconId, String title, boolean cancelable)
	{
		final int MAX_PROGRESS = 100;
		lineProgressDialog = new ProgressDialog(context);
		lineProgressDialog.setIcon(iconId);
		lineProgressDialog.setTitle(title);
		lineProgressDialog.setCancelable(cancelable);
		lineProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		lineProgressDialog.setMax(MAX_PROGRESS);
		lineProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface dialog)
			{
				LogFileUtil.v(MainApplication.TAG, "ProgressDialogUtil barDialogLine setOnDismissListener");
			}
		});
		lineProgressDialog.show();

		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				int Progress = 0;
				while (Progress < MAX_PROGRESS)
				{
					try
					{
						Thread.sleep(100); // 100ms 加 1
						Progress++;
						lineProgressDialog.incrementProgressBy(1);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				lineProgressDialog.dismiss(); // 关掉
			}
		};
		new Thread(run).start();
	}

	/** 这里在返回键中,返回键并不会被触发 */
	public static void closeDialog()
	{
		if (null != roundProgressDialog)
		{
			roundProgressDialog.dismiss();
		}

		if (null != lineProgressDialog)
		{
			lineProgressDialog.dismiss();
		}
	}
}
