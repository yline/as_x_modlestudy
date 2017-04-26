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
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

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
	 *
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
	 *
	 * @param context
	 * @param packagName
	 */
	public void killOtherProcess(Context context, String packagName)
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(packagName);
	}


	/**
	 * 拍照失败，则data不为null，其它值全部为null
	 * 拍照成功，则data为null
	 *
	 * @param activity
	 * @param fileName    暂存的图片名， camera_picture.jpg
	 * @param requestCode
	 * @return
	 */
	public static boolean openCamera(Activity activity, String fileName, int requestCode)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		Uri outputUri = Uri.fromFile(FileUtil.create(activity.getExternalCacheDir(), fileName));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri); // 存放位置为sdcard卡上cwj文件夹，文件名为android123.jpg格式
		if (null != intent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(intent, requestCode);
			return true;
		}
		else
		{
			LogFileUtil.e("IntentUtil", "camera do not exist");
			return false;
		}
	}

	/**
	 * 选择成功，返回data不为null, data.getdata 为null
	 * 选择失败，返回data为null
	 *
	 * @param activity
	 * @param requestCode
	 * @return
	 */
	public static boolean openAlbum(Activity activity, int requestCode)
	{
		Intent tempIntent = new Intent();
		tempIntent.setAction(Intent.ACTION_PICK);
		tempIntent.setType("image/*");
		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(tempIntent, requestCode);
			return true;
		}
		else
		{
			LogFileUtil.e("IntentUtil", "album do not exist");
			return false;
		}
	}

	/**
	 * 请求相册，并裁剪
	 * 选择成功，返回data不为null
	 * 选择失败，返回data为null
	 *
	 * @param activity
	 * @param uri         onActivityResult 返回的结果
	 * @param fileName    缓存本地的文件名
	 * @param requestCode
	 * @return
	 */
	public static boolean openPictureZoom(Activity activity, Uri uri, String fileName, int requestCode)
	{
		Intent tempIntent = new Intent("com.android.camera.action.CROP");

		tempIntent.setDataAndType(uri, "image/*");
		tempIntent.putExtra("crop", "true");

		Uri outputUri = Uri.fromFile(FileUtil.create(activity.getExternalCacheDir(), fileName));
		tempIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);// 图像输出
		tempIntent.putExtra("aspectX", 3); // 边长比例
		tempIntent.putExtra("aspectY", 3);
		tempIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		tempIntent.putExtra("noFaceDetection", true);
		tempIntent.putExtra("return-data", false);// 回调方法data.getExtras().getParcelable("data")返回数据为空
		if (null != tempIntent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(tempIntent, requestCode);
			return true;
		}
		else
		{
			LogFileUtil.e("IntentUtil", "album zoom do not exist");
			return false;
		}
	}

	/**
	 * 打开音乐浏览器,浏览文件
	 *
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
	 *
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
	 * 打开联系人界面
	 *
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
	 *
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
	 *
	 * @param context
	 */
	public void openRecord(Context context)
	{
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		context.startActivity(intent);
	}

	/**
	 * 打开网络设置界面
	 *
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
