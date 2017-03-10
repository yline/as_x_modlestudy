package com.sample.okhttp.httphelper;

import com.sample.okhttp.application.IApplication;
import com.yline.utils.FileUtil;

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
public class HttpPictureClient extends OkHttpClient
{
	/**
	 * 储存图片等较大内容
	 */
	private static final String PICTURE_CACHE_PATH = "Picture";

	private static OkHttpClient httpClient;

	// 2G 的最大缓存
	private static final int PICTURE_CACHE_SIZE = 2 * 1024 * 1024 * 1024;

	public static OkHttpClient getInstance()
	{
		if (null == httpClient)
		{
			synchronized (HttpPictureClient.class)
			{
				if (null == httpClient)
				{
					Builder builder = new Builder();

					String cacheDirStr = IApplication.getApplication().getExternalCacheDir() + File.separator + PICTURE_CACHE_PATH;
					File cacheDir = FileUtil.createFileDir(cacheDirStr);
					Cache cache = new Cache(cacheDir, PICTURE_CACHE_SIZE);
					builder.cache(cache);

					httpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	private HttpPictureClient()
	{
	}
}
