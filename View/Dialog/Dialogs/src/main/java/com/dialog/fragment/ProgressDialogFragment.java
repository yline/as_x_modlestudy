package com.dialog.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;

import com.view.dialog.R;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestFragment;

public class ProgressDialogFragment extends BaseTestFragment
{
	private ProgressDialog roundProgressDialog;

	private ProgressDialog lineProgressDialog;

	@Override
	protected void testStart()
	{
		addButton("进度 之 直线", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				roundProgressDialog = new ProgressDialog(getContext());
				roundProgressDialog.setIcon(R.mipmap.ic_launcher);
				roundProgressDialog.setTitle("进度条");
				roundProgressDialog.setCancelable(true);
				roundProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						LogFileUtil.v("ProgressDialogUtil roundProgressDialog onDismiss");
					}
				});
				roundProgressDialog.show();
			}
		});
		
		addButton("进度 之 环形", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				final int MAX_PROGRESS = 100;
				lineProgressDialog = new ProgressDialog(getContext());
				lineProgressDialog.setIcon(R.mipmap.ic_launcher);
				lineProgressDialog.setTitle("进度条");
				lineProgressDialog.setCancelable(true);
				lineProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				lineProgressDialog.setMax(MAX_PROGRESS);
				lineProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(DialogInterface dialog)
					{
						LogFileUtil.v("ProgressDialogUtil barDialogLine setOnDismissListener");
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
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						lineProgressDialog.dismiss(); // 关掉
					}
				};
				new Thread(run).start();
			}
		});
	}

	/* 这里在返回键中,返回键并不会被触发*/
	public void closeDialog()
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
