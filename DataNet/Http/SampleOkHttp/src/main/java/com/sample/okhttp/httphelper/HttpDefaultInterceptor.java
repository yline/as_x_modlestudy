package com.sample.okhttp.httphelper;

import com.sample.okhttp.http.XHttp;
import com.yline.log.LogFileUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpDefaultInterceptor implements Interceptor
{
	private boolean isDebug = XHttp.isDebug;

	@Override
	public Response intercept(Chain chain) throws IOException
	{
		Request request = chain.request();
		long time1 = System.nanoTime();
		if (isDebug)
		{
			LogFileUtil.v(String.format("request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
		}

		Response response = chain.proceed(request);
		// 打印日志
		if (isDebug)
		{
			long time2 = System.nanoTime();

			LogFileUtil.v(String.format("response %s in %.1fms%n%s", response.request().url(), (time2 - time1) / 1e6d, response.headers()));
		}

		// 设置缓存

		return response;
	}
}
