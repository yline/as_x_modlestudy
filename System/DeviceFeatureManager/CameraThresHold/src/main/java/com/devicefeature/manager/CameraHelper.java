package com.devicefeature.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.devicefeature.manager.activity.MainApplication;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 系统Camera
 * Created by yline on 2016/10/31.
 */
public class CameraHelper
{
	public static final int TASK_PHOTO = 1;

	public static final int CROP_PHOTO = 2;

	public static final String TAG = "CameraHelper";

	/**
	 * 开启剪辑照片,在摄像,本地有图片的情形下进行
	 * @param fileName
	 * @param requestCode
	 */
	public Uri cropPhoto(Activity activity, String fileName, int requestCode)
	{
		Uri uri = null;
		if (TextUtils.isEmpty(fileName))
		{
			LogFileUtil.e(TAG, "fileName is empty, take photo failed");
			return uri;
		}

		String savePath = FileUtil.getPath();
		if (null == savePath)
		{
			LogFileUtil.e(TAG, "the sdcard is null, take photo failed");
			return uri;
		}

		File saveFile = new File(savePath + fileName + ".jpg");
		if (!saveFile.exists())
		{
			MainApplication.toast("the saveFile is null, crop photo failed");
			return uri;
		}

		uri = Uri.fromFile(saveFile);

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(intent, requestCode); // 启动裁剪程序

		return uri;
	}

	/**
	 * 开启摄像
	 * @param fileName
	 * @param requestCode
	 */
	public boolean takePhoto(Activity activity, String fileName, int requestCode)
	{
		if (TextUtils.isEmpty(fileName))
		{
			LogFileUtil.e(TAG, "fileName is empty, take photo failed");
			return false;
		}

		String savePath = FileUtil.getPath();
		if (null == savePath)
		{
			LogFileUtil.e(TAG, "the sdcard is null, take photo failed");
			return false;
		}

		File saveFile = new File(savePath + fileName + ".jpg");
		if (saveFile.exists())
		{
			saveFile.delete();
		}

		try
		{
			saveFile.createNewFile();

			// 启动相机程序
			Uri uri = Uri.fromFile(saveFile);
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			activity.startActivityForResult(intent, requestCode);

			return true;
		}
		catch (IOException e)
		{
			LogFileUtil.e(TAG, "saveFile error IOException");
			return false;
		}
	}


	/**
	 * 未使用
	 * 作用:用户Crop Intent之后,在onActivityResult方法中可以获取到Bitmap
	 * @param context
	 * @param uri
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap getCropBitmap(Context context, Uri uri) throws FileNotFoundException
	{
		InputStream inputStream = context.getContentResolver().openInputStream(uri);
		if (null == inputStream)
		{
			LogFileUtil.e(TAG, "getCropBitmap inputStream is null");
			return null;
		}

		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * 未使用
	 * 作用:设置,摄像头使用前置还是后置
	 * @param isFront 前置
	 */
	@SuppressLint("NewApi")
	public void setCameraDirection(android.hardware.Camera camera, Boolean isFront)
	{
		if (isFront == true)
		{
			camera = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
		}
		else
		{
			camera = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
		}
	}

	/**
	 * 未使用
	 * 作用:转换Camera摄影角度
	 * @param camera 摄像头
	 * @param angle  角度 0-360
	 */
	public void setDisplayOrientation(Camera camera, int angle)
	{
		Method downPolymorphic;

		try
		{
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
			if (downPolymorphic != null)
			{
				downPolymorphic.invoke(camera, new Object[]{angle});   // 反射
			}
		}
		catch (NoSuchMethodException e)
		{
			LogFileUtil.e(TAG, "setDisplayOrientation NoSuchMethodException", e);
		}
		catch (IllegalAccessException e)
		{
			LogFileUtil.e(TAG, "setDisplayOrientation IllegalAccessException", e);
		}
		catch (InvocationTargetException e)
		{
			LogFileUtil.e(TAG, "setDisplayOrientation InvocationTargetException", e);
		}
	}
}
