package com.sample.https.connection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yline on 2017/2/6.
 */
public class HttpsHelper
{
	/**
	 * 读取请求头
	 * @param httpsURLConnection
	 * @return null if httpsURLConnection is null
	 */
	public static String getHttpsRequestHeader(HttpsURLConnection httpsURLConnection)
	{
		if (null != httpsURLConnection)
		{
			Map<String, List<String>> requestHeaderMap = httpsURLConnection.getRequestProperties();
			Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
			StringBuilder requestHeaderStringBuilder = new StringBuilder();
			while (requestHeaderIterator.hasNext())
			{
				String requestHeaderKey = requestHeaderIterator.next();
				String requestHeaderValue = httpsURLConnection.getRequestProperty(requestHeaderKey);
				requestHeaderStringBuilder.append(requestHeaderKey);
				requestHeaderStringBuilder.append(":");
				requestHeaderStringBuilder.append(requestHeaderValue);
				requestHeaderStringBuilder.append("\n");
			}
			return requestHeaderStringBuilder.toString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * 读取响应头
	 * @param httpsURLConnection
	 * @return null if httpsURLConnection is null
	 */
	public static String getHttpsResponseHeader(HttpsURLConnection httpsURLConnection)
	{
		if (null != httpsURLConnection)
		{
			Map<String, List<String>> responseHeaderMap = httpsURLConnection.getHeaderFields();
			int size = responseHeaderMap.size();
			StringBuilder responseHeaderStringBuilder = new StringBuilder();
			for (int i = 0; i < size; i++)
			{
				String responseHeaderKey = httpsURLConnection.getHeaderFieldKey(i);
				String responseHeaderValue = httpsURLConnection.getHeaderField(i);
				responseHeaderStringBuilder.append(responseHeaderKey);
				responseHeaderStringBuilder.append(":");
				responseHeaderStringBuilder.append(responseHeaderValue);
				responseHeaderStringBuilder.append("\n");
			}
			return responseHeaderStringBuilder.toString();
		}
		else
		{
			return null;
		}
	}
}
