package com.sample.okhttp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sample.okhttp.http.XHttp;
import com.yline.test.BaseTestFragment;

import java.util.HashMap;
import java.util.Map;

public class TestGetFragment extends BaseTestFragment
{

	@Override
	protected void testStart(View view, Bundle savedInstanceState)
	{
		final TextView tvShow = addTextView("test get");
		addButton("测试get请求(带缓存一起)", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new XHttp<String>()
				{
					@Override
					public void onSuccess(String s)
					{
						tvShow.setText(s);
					}

					@Override
					protected boolean isResponseCodeHandler()
					{
						return false;
					}

					@Override
					protected String getRequestUrlBase()
					{
						return "https://api.douban.com/v2/movie/top250";
					}

					@Override
					protected Map<String, String> getRequestGetParam()
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("start", "0");
						map.put("count", "1");
						return map;
					}

					@Override
					protected boolean isResponseParse()
					{
						return false;
					}

					@Override
					protected int getRequestType()
					{
						return XHttp.REQUEST_GET;
					}
				}.doRequest("", String.class);
			}
		});
	}
}
