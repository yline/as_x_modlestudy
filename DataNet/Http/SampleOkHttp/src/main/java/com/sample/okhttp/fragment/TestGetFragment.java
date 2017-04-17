package com.sample.okhttp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sample.okhttp.http.XHttpAdapter;
import com.sample.okhttp.http.XHttpUtil;
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
				Map<String, String> map = new HashMap<String, String>();
				map.put("start", "0");
				map.put("count", "1");

				XHttpUtil.doDoubanTest(map, new XHttpAdapter<String>()
				{
					@Override
					public void onSuccess(String s)
					{
						tvShow.setText(s);
					}
				});
			}
		});
	}
}
