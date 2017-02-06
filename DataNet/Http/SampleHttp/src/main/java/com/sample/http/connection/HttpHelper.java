package com.sample.http.connection;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yline on 2017/2/6.
 */
public class HttpHelper
{
	/**
	 * 读取请求头
	 * @param connection
	 * @return null if connection is null
	 */
	public static String getHttpRequestHeader(HttpURLConnection connection)
	{
		if (null != connection)
		{
			Map<String, List<String>> requestHeaderMap = connection.getRequestProperties();
			Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
			StringBuilder requestHeaderStringBuilder = new StringBuilder();
			while (requestHeaderIterator.hasNext())
			{
				String requestHeaderKey = requestHeaderIterator.next();
				String requestHeaderValue = connection.getRequestProperty(requestHeaderKey);
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
	 * @param connection
	 * @return null if connection is null
	 */
	public static String getHttpResponseHeader(HttpURLConnection connection)
	{
		if (null != connection)
		{
			Map<String, List<String>> responseHeaderMap = connection.getHeaderFields();
			int size = responseHeaderMap.size();
			StringBuilder responseHeaderStringBuilder = new StringBuilder();
			for (int i = 0; i < size; i++)
			{
				String responseHeaderKey = connection.getHeaderFieldKey(i);
				String responseHeaderValue = connection.getHeaderField(i);
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
