package com.sample.okhttp.activity;

import android.os.Bundle;
import android.view.View;

import com.sample.okhttp.R;
import com.sample.okhttp.application.IApplication;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yline on 2017/2/7.
 */
public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				OkHttpClient okHttpClient = new OkHttpClient();

				// String httpUrl = "http://120.92.77.154/crest/index.php/api/example/users/N#887a19d10a6601b2";
				String httpUrl = "https://github.com/yissan/CalendarView";
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
		});
	}
}
