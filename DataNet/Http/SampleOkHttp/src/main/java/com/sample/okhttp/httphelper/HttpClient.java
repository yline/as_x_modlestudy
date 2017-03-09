package com.sample.okhttp.httphelper;

import okhttp3.OkHttpClient;

/**
 * 提供,统一处理Http的Client
 *
 * @author yline 2017/2/28 --> 17:29
 * @version 1.0.0
 */
public class HttpClient extends OkHttpClient
{
	private HttpClient()
	{
	}
	
	public static OkHttpClient getInstance()
	{
		return OkHttpClientHold.sInstance;
	}

	private static class OkHttpClientHold
	{
		private static OkHttpClient sInstance = new HttpClient();
	}
}
