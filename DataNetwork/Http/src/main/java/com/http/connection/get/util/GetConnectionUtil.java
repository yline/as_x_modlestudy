package com.http.connection.get.util;

import android.os.Handler;

import com.http.help.util.HttpHelperUtils;
import com.yline.log.LogFileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 请求的URL,区分大小写
 *
 * @author YLine
 *         <p/>
 *         2016年7月2日 下午12:04:15
 */
public class GetConnectionUtil
{
	private static final String TAG = "connection_get";

	private static final int HTTPCONNECT_SUCCESS_CODE = 200; // 网络请求,正确返回码

	private static final int CONNNECT_TIMEOUT = 5000; // 连接超时时间 ms

	private static final int READ_TIMEOUT = 5000; // 读取超时时间 ms

	private static Handler mHandler = new Handler();

	public static void doLocal(String ip, String projectName, String classStr, final GetConnectionCallback callback)
	{
		String httpUrl = String.format("http://%s:8080/%s/%s", ip, projectName, classStr);
		doGetAsyn(httpUrl, callback);
	}

	/**
	 * Get 异步请求
	 *
	 * @param httpUrl  URL
	 * @param callback 网络请求回调
	 */
	private static void doGetAsyn(final String httpUrl, final GetConnectionCallback callback)
	{
		LogFileUtil.v(TAG, "doGetAsyn -> in -> httpUrl:" + httpUrl);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				URL url = null;
				HttpURLConnection httpURLConnection = null;

				BufferedReader bufferedReader = null;

				StringBuffer result = new StringBuffer();

				try
				{
					url = new URL(httpUrl); // 创建一个URL对象
					LogFileUtil.v(TAG, "doGetAsyn -> new url over");

					// 调用URL的openConnection()方法,获取HttpURLConnection对象
					httpURLConnection = (HttpURLConnection) url.openConnection();

					// HttpURLConnection默认就是用GET发送请求,因此也可省略
					httpURLConnection.setRequestMethod("GET");
					initHttpConnection(httpURLConnection);
					/** 请求头,日志 */
					String requestHeader = HttpHelperUtils.getHttpRequestHeader(httpURLConnection);
					LogFileUtil.i(TAG, "doGetAsyn -> initHttpConnection over requestHeader\n" + requestHeader);

					int responseCode = httpURLConnection.getResponseCode();
					LogFileUtil.i(TAG, "doGetAsyn -> responseCode = " + responseCode);

					if (HTTPCONNECT_SUCCESS_CODE == responseCode) // 连接成功
					{
						bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
						String line = null;
						while (null != (line = bufferedReader.readLine())) // 即:有数据时,一直在读取
						{
							result.append(line);
						}

						/** 响应头,日志 */
						String responseHeader = HttpHelperUtils.getHttpResponseHeader(httpURLConnection);
						LogFileUtil.i(TAG, "doGetAsyn -> responseHeader\n" + responseHeader);

						handleSuccess(callback, result.toString());
					}
					else
					{
						LogFileUtil.e(TAG, "doGetAsyn -> responseCode error");
						handleError(callback, new Exception("doGetAsyn -> responseCode error code = " + responseCode));
					}
				}
				catch (MalformedURLException e)
				{
					LogFileUtil.e(TAG, "doGetAsyn -> MalformedURLException", e);
					handleError(callback, e);
				}
				catch (ProtocolException e)
				{
					LogFileUtil.e(TAG, "doGetAsyn -> ProtocolException", e);
					handleError(callback, e);
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "doGetAsyn -> IOException", e);
					handleError(callback, e);
				}
				finally
				{
					if (null != bufferedReader)
					{
						try
						{
							bufferedReader.close();
						}
						catch (IOException e)
						{
							LogFileUtil.e(TAG, "doGetAsyn -> close IOException", e);
							handleError(callback, e);
						}
					}
					if (null != httpURLConnection)
					{
						httpURLConnection.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * http 链接  设置
	 * RequestHeader : setRequestProperty
	 * parameter     : 直接在后面加的参数
	 *
	 * @param connection
	 */
	private static void initHttpConnection(HttpURLConnection connection)
	{
		// 用setRequestProperty方法设置多个自定义的请求头:action，用于后端判断
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("action", "get");

		// 禁用网络缓存
		connection.setUseCaches(false);
		// 定制,只要设置了下面这句话,系统就会自动改为POST请求..因此,必须注释掉
		// connection.setDoOutput(true); // 设置此方法,允许向服务器输出内容

		// HttpURLConnection默认也支持从服务端读取结果流，因此也可省略
		connection.setDoInput(true);
		connection.setReadTimeout(READ_TIMEOUT); // 设置读取超时为5秒
		connection.setConnectTimeout(CONNNECT_TIMEOUT); // 设置连接网络超时为5秒
	}

	/**
	 * 回调,抛出异常到主线程
	 *
	 * @param callback
	 * @param e
	 */
	private static void handleError(final GetConnectionCallback callback, final Exception e)
	{
		mHandler.post(new Runnable()
		{

			@Override
			public void run()
			{
				if (null != callback)
				{
					callback.onError(e);
				}
			}
		});
	}

	/**
	 * 回调,抛出网络请求结果到主线程
	 *
	 * @param callback
	 * @param result
	 */
	private static void handleSuccess(final GetConnectionCallback callback, final String result)
	{
		mHandler.post(new Runnable()
		{

			@Override
			public void run()
			{
				if (null != callback)
				{
					callback.onSuccess(result);
				}
			}
		});
	}

	public interface GetConnectionCallback
	{
		/**
		 * 请求成功,返回结果
		 *
		 * @param result
		 */
		void onSuccess(String result);

		/**
		 * 网络错误
		 *
		 * @param e
		 */
		void onError(Exception e);
	}
}
