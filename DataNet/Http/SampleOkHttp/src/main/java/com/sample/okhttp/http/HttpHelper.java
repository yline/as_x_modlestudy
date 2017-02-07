package com.sample.okhttp.http;

import com.sample.okhttp.application.IApplication;
import com.yline.log.LogFileUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yline 2017/2/7 --> 19:07
 * @version 1.0.0
 */
public class HttpHelper
{
	public static void doGet(String httpUrl)
	{
		OkHttpClient okHttpClient = new OkHttpClient();

		final Request request = new Request.Builder().url(httpUrl).build();

		Call call = okHttpClient.newCall(request);
		// 异步调度,两个回调是在 子线程中执行的
		call.enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				LogFileUtil.e("", "", e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				String htmlStr = response.body().string();
				LogFileUtil.v(htmlStr);
				IApplication.toast(htmlStr);

				LogFileUtil.v(response.headers().toString());
			}
		});
	}
}



