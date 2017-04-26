package com.manager.Test;

import android.content.Context;

import com.manager.APNManager;
import com.manager.activity.MainApplication;
import com.yline.log.LogFileUtil;

public class APNManagerUser
{
	public static void test(final Context context)
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				/*  权限被否
				LogFileUtil.v(APNManager.TAG, "-------------------All of APNs----------------------------");
				APNManager.getAPNList(context);

				LogFileUtil.v(APNManager.TAG, "-------------------Available of APNs----------------------------");
				APNManager.getAvailableAPNList(context);
				*/
				LogFileUtil.v(APNManager.TAG, "-------------------Currents of APN----------------------------");
				String currentId = APNManager.getCurrentApnId(context);
				LogFileUtil.v(APNManager.TAG, "The Current ID = " + currentId);

				LogFileUtil.v(APNManager.TAG, "-------------------WAP APN ID----------------------------");
				APNManager.getWapApnId(context);
				/* 权限被否
				LogFileUtil.v(APNManager.TAG, "-------------------Switch APN By Name----------------------------");
				boolean resultSwitchByName = APNManager.switchApnByName(context, "公安接入点");
				LogFileUtil.v(APNManager.TAG, "The Result of switch by name = " + currentId);
				*/
				LogFileUtil.v(APNManager.TAG, "-------------------Switch APN By ID----------------------------");
				APNManager.setCurrentApn(context, "-1");
				LogFileUtil.v(APNManager.TAG, "The Result of switch by name = " + currentId);

				MainApplication.toast("APNManaget Test End");
			}
		};

		new Thread(runnable).start();
	}
}
