package com.lrucache.activity;

import com.lrucache.ImageLoader;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/12/6.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "LruCache";

	private static ImageLoader imageLoader;

	@Override
	public void onCreate()
	{
		super.onCreate();
		imageLoader = new ImageLoader(this);
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}

	public static ImageLoader getImageLoader()
	{
		return imageLoader;
	}
}
