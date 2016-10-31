package com.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import com.tools.activity.MainApplication;

/**
 * 跳转到一些系统的应用
 * BaseAppcalition被使用
 */
public class SystemSkipTool
{
	private static final String HINE_JUMP_FAILED = "跳转失败";

	public SystemSkipTool()
	{
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
		if (tempIntent.resolveActivity(context.getPackageManager()) != null)
		{
			context.startActivity(tempIntent);
		}
		else
		{
			MainApplication.toast("检查是否安装浏览器");
		}
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
		if (tempIntent.resolveActivity(activity.getPackageManager()) != null)
		{
			activity.startActivityForResult(tempIntent, requestCode);
		}
		else
		{
			MainApplication.toast(HINE_JUMP_FAILED);
		}
	}

	/**
	 * 裁剪图片
	 * @param uri     被裁减的照片的url
	 * @param backUri 裁剪后照片存放位置的uri
	 */
	public void openAlbumZoom(Activity activity, Uri uri, Uri backUri, int requestCode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("inputX", 400);
		intent.putExtra("inputY", 400);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, backUri);//图像输出
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);//回调方法data.getExtras().getParcelable("data")返回数据为空
		activity.startActivityForResult(intent, requestCode);
	}

	/**
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
			MainApplication.toast(HINE_JUMP_FAILED);
		}
	}

	/**
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
			MainApplication.toast(HINE_JUMP_FAILED);
		}
	}

	/**
	 * 打开网络设置界面
	 * @param requestCode 请求码
	 */
	public void openSetting(Activity activity, int requestCode)
	{
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);

		if (null != intent.resolveActivity(activity.getPackageManager()))
		{
			activity.startActivityForResult(intent, requestCode);
		}
		else
		{
			MainApplication.toast(HINE_JUMP_FAILED);
		}
	}
}
