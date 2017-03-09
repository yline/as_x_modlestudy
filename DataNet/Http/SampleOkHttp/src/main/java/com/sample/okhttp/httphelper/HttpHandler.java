package com.sample.okhttp.httphelper;


import android.os.Handler;

/**
 * 提供,统一处理Http的handler
 *
 * @author yline 2017/2/23 --> 10:26
 * @version 1.0.0
 */
public class HttpHandler extends Handler
{
	private HttpHandler()
	{
	}

	public static HttpHandler build()
	{
		return HttpHandlerHold.sInstance;
	}

	private static class HttpHandlerHold
	{
		private static HttpHandler sInstance = new HttpHandler();
	}
}
