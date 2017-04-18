package com.nohttp.okhttp;

import com.nohttp.okhttp.url.internal.huc.OkHttpURLConnection;
import com.nohttp.okhttp.url.internal.huc.OkHttpsURLConnection;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import okhttp3.OkHttpClient;

public class URLConnectionFactory implements Cloneable
{

	private static URLConnectionFactory instance;

	/**
	 * Gets instance.
	 *
	 * @return {@link URLConnectionFactory}.
	 */
	public static URLConnectionFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (URLConnectionFactory.class)
			{
				if (instance == null)
				{
					instance = new URLConnectionFactory(new OkHttpClient());
				}
			}
		}
		return instance;
	}

	private OkHttpClient mClient;

	public URLConnectionFactory(OkHttpClient client)
	{
		this.mClient = client;
	}

	/**
	 * Gets OkHttpClient.
	 *
	 * @return {@link OkHttpClient}.
	 */
	public OkHttpClient client()
	{
		return mClient;
	}

	@Override
	public URLConnectionFactory clone()
	{
		return new URLConnectionFactory(mClient);
	}

	/**
	 * Open url.
	 *
	 * @param url {@link URL}.
	 * @return {@link HttpURLConnection}.
	 */
	public HttpURLConnection open(URL url)
	{
		return open(url, null);
	}

	/**
	 * Open url.
	 *
	 * @param url   {@link URL}.
	 * @param proxy {@link Proxy}.
	 * @return {@link HttpURLConnection}.
	 */
	public HttpURLConnection open(URL url, Proxy proxy)
	{
		OkHttpClient copy = mClient.newBuilder().proxy(proxy).build();

		String protocol = url.getProtocol();
		if (protocol.equals("http"))
		{
			return new OkHttpURLConnection(url, copy);
		}
		if (protocol.equals("https"))
		{
			return new OkHttpsURLConnection(url, copy);
		}
		throw new IllegalArgumentException("Unexpected protocol: " + protocol);
	}
}
