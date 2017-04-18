package com.nohttp.okhttp;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Network;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;
import com.yanzhenjie.nohttp.tools.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class OkHttpNetwork implements Network
{
	private HttpURLConnection mUrlConnection;

	public OkHttpNetwork(HttpURLConnection urlConnection)
	{
		this.mUrlConnection = urlConnection;
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return mUrlConnection.getOutputStream();
	}

	@Override
	public int getResponseCode() throws IOException
	{
		return mUrlConnection.getResponseCode();
	}

	@Override
	public Map<String, List<String>> getResponseHeaders()
	{
		return mUrlConnection.getHeaderFields();
	}

	@Override
	public InputStream getServerStream(int responseCode, Headers headers) throws IOException
	{
		return URLConnectionNetworkExecutor.getServerStream(responseCode, headers.getContentEncoding(),
				mUrlConnection);
	}

	@Override
	public void close() throws IOException
	{
		IOUtils.closeQuietly(mUrlConnection);
	}

}
