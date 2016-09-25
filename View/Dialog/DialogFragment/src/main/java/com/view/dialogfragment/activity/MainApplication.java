package com.view.dialogfragment.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/9/25.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "DialogFragment";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
