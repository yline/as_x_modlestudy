package com.okhttp.php.http;

import com.okhttp.php.application.AppStateManager;
import com.yline.http.XHttpAdapter;
import com.yline.http.helper.XTextHttp;

public class XHttpUtils
{
	public static void doPhpTest80(XHttpAdapter<String> adapter)
	{
		String httpUrl = String.format("http://%s:80/test/yline.php", AppStateManager.getInstance().getServerIp());
		new XTextHttp<String>(adapter)
		{
			@Override
			protected boolean isResponseCodeHandler()
			{
				return false;
			}

			@Override
			protected boolean isResponseParse()
			{
				return false;
			}
		}.doGet(httpUrl, null, String.class);
	}

	public static void doPhpTest8080(XHttpAdapter<String> adapter)
	{
		String httpUrl = String.format("http://%s:8080/yline.php", AppStateManager.getInstance().getServerIp());
		new XTextHttp<String>(adapter)
		{
			@Override
			protected boolean isResponseCodeHandler()
			{
				return false;
			}

			@Override
			protected boolean isResponseParse()
			{
				return false;
			}
		}.doGet(httpUrl, null, String.class);
	}
}
