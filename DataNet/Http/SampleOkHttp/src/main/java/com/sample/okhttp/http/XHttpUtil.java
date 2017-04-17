package com.sample.okhttp.http;

import com.sample.okhttp.bean.VNewsSingleBean;
import com.sample.okhttp.httphelper.XTextHttp;

import java.util.Map;

public class XHttpUtil
{
	public static void doDoubanTest(Map<String, String> map, XHttpAdapter adapter)
	{
		String httpUrl = "https://api.douban.com/v2/movie/top250";

		new XTextHttp<String>(adapter)
		{
			@Override
			protected boolean isResponseParse()
			{
				return false;
			}

			@Override
			protected boolean isResponseCodeHandler()
			{
				return false;
			}
		}.doGet(httpUrl, map, String.class);
	}

	public static void doNews(XHttpAdapter adapter)
	{
		String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";
		new XTextHttp<VNewsSingleBean>(adapter).doPost(httpUrl, null, VNewsSingleBean.class);
	}
}
