package com.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by yline on 2016/10/29.
 */
public class SystemIntentUtil
{
	/**
	 * 安装Apk
	 * @param context
	 * @param path    /storage/sdcard1/临时文件夹/ActivityBackCode.apk
	 */
	public static void installApk(Context context, String path)
	{
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");

		context.startActivity(intent);
	}

	/**
	 * kill Process myself
	 */
	public static void killMineProcess()
	{
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * kill Process others
	 * @param context
	 * @param packagName
	 */
	public static void killOtherProcess(Context context, String packagName)
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(packagName);
	}
}
