package com.device.bluetooth.activity;

import com.device.bluetooth.BlueToothHelper;
import com.device.bluetooth.receiver.BlueToothReceiver;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/11/4.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "BlueToothSample";

	@Override
	public void onCreate()
	{
		super.onCreate();

		BlueToothHelper.getInstance().registerReceiver(this, new BlueToothReceiver());
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
