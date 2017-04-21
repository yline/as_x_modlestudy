package com.sample.okhttp.httphelper;

import com.yline.application.SDKManager;
import com.yline.utils.FileUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 提供,统一处理Http的Client;
 * 这个用来处理文字和不被清除的内存(排除图片+视频)
 *
 * @author yline 2017/2/28 --> 17:29
 * @version 1.0.0
 */
public class HttpTextClient extends OkHttpClient
{
	/**
	 * 储存文字等内容
	 */
	private static final String DEFAULT_CACHE_PATH = "Text";

	private static OkHttpClient httpClient;

	private static final int DEFAULT_CACHE_SIZE = 512 * 1024 * 1024;

	public static OkHttpClient getInstance()
	{
		if (null == httpClient)
		{
			synchronized (HttpTextClient.class)
			{
				if (null == httpClient)
				{
					OkHttpClient.Builder builder = new OkHttpClient.Builder();

					// 设置缓存
					String cacheDirStr = SDKManager.getApplication().getExternalCacheDir() + File.separator + DEFAULT_CACHE_PATH;
					File cacheDir = FileUtil.createDir(cacheDirStr);
					Cache cache = new Cache(cacheDir, DEFAULT_CACHE_SIZE);
					builder.cache(cache);
					// 设置超时
					builder.connectTimeout(10, TimeUnit.SECONDS)
							.readTimeout(10, TimeUnit.SECONDS)
							.writeTimeout(10, TimeUnit.SECONDS);

					// 添加拦截器
					builder.addInterceptor(new HttpTextInterceptor());  // 有网络、没网络都会走
					builder.addNetworkInterceptor(new HttpTextNetworkInterceptor()); // 有网络,会走

					httpClient = builder.build();
				}
			}
		}
		return httpClient;
	}

	private HttpTextClient()
	{
	}
}
