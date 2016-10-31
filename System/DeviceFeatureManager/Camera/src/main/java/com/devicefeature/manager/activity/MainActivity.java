package com.devicefeature.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.devicefeature.manager.CameraHelper;
import com.devicefeature.manager.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private CameraHelper cameraHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cameraHelper = new CameraHelper();
		findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_tack_photo");
				cameraHelper.takePhoto(MainActivity.this, "temp", CameraHelper.TASK_PHOTO);
			}
		});

		findViewById(R.id.btn_crop_photo).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_crop_photo");
				cameraHelper.cropPhoto(MainActivity.this, "temp", CameraHelper.CROP_PHOTO);
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		LogFileUtil.v("onActivityResult requestCode = " + requestCode + ",resultCode = " + resultCode);
		if (null != data)
		{
			if (RESULT_OK == resultCode)
			{
				switch (requestCode)
				{
					case CameraHelper.TASK_PHOTO:
						MainApplication.toast("take photo success");
						break;
					case CameraHelper.CROP_PHOTO:
						MainApplication.toast("crop photo success");
						break;
				}
			}
		}

	}
}
