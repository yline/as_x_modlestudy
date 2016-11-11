package com.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.provider.Settings;

import com.tools.activity.MainApplication;

import java.io.File;

/**
 * @author yline 2016/11/11 --> 19:08
 * @version 1.0.0
 */
public class IntentHelper
{
	private static final String HINT_JUMP_FAILED = "跳转失败";

	private IntentHelper()
	{
	}

	public static IntentHelper getInstance()
	{
		return IntentHelperHold.sInstance;
	}

	private static class IntentHelperHold
	{
		private static IntentHelper sInstance = new IntentHelper();
	}

	/**
	 * 安装Apk;未测试
	 * @param context
	 * @param path    /storage/sdcard1/临时文件夹/ActivityBackCode.apk
	 */
	public void installApk(Context context, String path)
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
	public void killMineProcess()
	{
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * kill Process others;未测试
	 * @param context
	 * @param packagName
	 */
	public void killOtherProcess(Context context, String packagName)
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(packagName);
	}

	/**
	 * 打开相册
	 * @param requestCode 请求码
	 */
	public void openAlbum(Activity activity, int requestCode)
	{
		Intent tempIntent = new Intent();
		tempIntent.setAction(Intent.ACTION_PICK);
		tempIntent.setType("image/*");
		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(tempIntent, requestCode);
		}
		else
		{
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}

	/**
	 * 裁剪图片
	 * @param uri     被裁减的照片的url
	 * @param backUri 裁剪后照片存放位置的uri
	 */
	public void openAlbumZoom(Activity activity, Uri uri, Uri backUri, int requestCode)
	{
		Intent tempIntent = new Intent("com.android.camera.action.CROP");
		tempIntent.setDataAndType(uri, "image/*");
		tempIntent.putExtra("crop", "true");
		tempIntent.putExtra("inputX", 400);
		tempIntent.putExtra("inputY", 400);
		tempIntent.putExtra(MediaStore.EXTRA_OUTPUT, backUri);//图像输出
		tempIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		tempIntent.putExtra("noFaceDetection", true);
		tempIntent.putExtra("return-data", false);// 回调方法data.getExtras().getParcelable("data")返回数据为空
		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(tempIntent, requestCode);
		}
		else
		{
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}

	/**
	 * 打开音乐浏览器,浏览文件
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
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}

	/**
	 * 浏览器
	 * @param website http:// website
	 * @return
	 */
	public void openBrower(Context context, String website)
	{
		Intent tempIntent = new Intent();
		tempIntent.setAction(Intent.ACTION_VIEW);

		Uri tempUri;
		if ("http://".equals(website.substring(0, 7)) || "https://".equals(website.substring(0, 8)))
		{
			tempUri = Uri.parse(website);
		}
		else
		{
			tempUri = Uri.parse("https://" + website);
		}
		tempIntent.setData(tempUri);

		// 跳转
		if (null != tempIntent.resolveActivity(context.getPackageManager()))
		{
			context.startActivity(tempIntent);
		}
		else
		{
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}

	/**
	 * 打开摄像机
	 * @param activity
	 * @param uri
	 * @param requestCode
	 */
	public void openCamera(Activity activity, Uri uri, int requestCode)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //存放位置为sdcard卡上cwj文件夹，文件名为android123.jpg格式

		if (null != intent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(intent, requestCode);
		}
		else
		{
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}

	/**
	 * 打开联系人界面
	 * @param context
	 */
	public void openContact(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Contacts.People.CONTENT_URI);
		context.startActivity(intent);
	}

	/**
	 * 打开 文件管理器(包含音频、图片、视频等等)
	 * @param requestCode 请求码
	 */
	public void openFile(Activity activity, int requestCode)
	{
		Intent tempIntent = new Intent();
		tempIntent.setAction(Intent.ACTION_GET_CONTENT);
		tempIntent.setType("*/*");
		tempIntent.addCategory(Intent.CATEGORY_OPENABLE); // 可选择的选项

		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			Intent target = Intent.createChooser(tempIntent, "chooser");
			activity.startActivityForResult(target, requestCode);
		}
		else
		{
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}

	/**
	 * 打开录音器
	 * @param context
	 */
	public void openRecord(Context context)
	{
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		context.startActivity(intent);
	}

	/**
	 * 打开网络设置界面
	 * @param requestCode 请求码
	 */
	public void openSettingWifi(Activity activity, int requestCode)
	{
		Intent tempIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);

		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(tempIntent, requestCode);
		}
		else
		{
			MainApplication.toast(HINT_JUMP_FAILED);
		}
	}
}
