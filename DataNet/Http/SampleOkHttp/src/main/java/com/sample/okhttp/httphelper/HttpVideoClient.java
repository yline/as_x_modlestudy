package com.sample.okhttp.httphelper;

import com.sample.okhttp.application.IApplication;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 提供,统一处理Http的Client;
 * 这个用来处理图片内容
 *
 * @author yline 2017/2/28 --> 17:29
 * @version 1.0.0
 */
public class HttpVideoClient extends OkHttpClient
{
	/**
	 * 储存图片等较大内容
	 */
	private static final String VIDEO_CACHE_PATH = "Video";

	private static OkHttpClient httpClient;

	// 1T 的最大容量
	private static final int VIDEO_CACHE_SIZE = 1024 * 1024 * 1024 * 1024;

	public static OkHttpClient getInstance()
	{
		if (null == httpClient)
		{
			synchronized (HttpVideoClient.class)
			{
				if (null == httpClient)
				{
					Builder builder = new Builder();

					File cacheDir = IApplication.getApplication().getExternalFilesDir(VIDEO_CACHE_PATH);
					Cache cache = new Cache(cacheDir, VIDEO_CACHE_SIZE);
					builder.cache(cache);

					httpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	private HttpVideoClient()
	{
	}
}
