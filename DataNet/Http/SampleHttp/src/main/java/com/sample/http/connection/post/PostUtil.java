package com.sample.http.connection.post;

import android.os.Handler;

import com.google.gson.Gson;
import com.sample.http.connection.HttpHelper;
import com.yline.log.LogFileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 请求的URL,区分大小写
 * @author YLine
 *         <p/>
 *         2016年7月3日 上午9:40:08
 */
public class PostUtil
{
	private static final String TAG = "connection_post";

	private static final int HTTPCONNECT_SUCCESS_CODE = 200; // 网络请求,正确返回码

	private static final int CONNNECT_TIMEOUT = 5000; // 连接超时时间 ms

	private static final int READ_TIMEOUT = 5000; // 读取超时时间 ms

	private static Handler mHandler = new Handler();

	public static void doLocal(String ip, String projectName, String classStr, final PostConnectionCallback callback)
	{
		String httpUrl = String.format("http://%s:8080/%s/%s", ip, projectName, classStr);
		doPostAsyn(httpUrl, callback);
	}

	/**
	 * Post 异步请求
	 * @param httpUrl  URL
	 * @param callback 网络请求回调
	 */
	private static void doPostAsyn(final String httpUrl, final PostConnectionCallback callback)
	{
		LogFileUtil.v(TAG, "doPostAsyn -> in -> http:" + httpUrl);

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
					url = new URL(httpUrl);
					LogFileUtil.v(TAG, "doPostAsyn -> new url over");

					httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setRequestMethod("POST");
					initHttpConnection(httpURLConnection);

					/** 请求头,日志 */
					String requestHeader = HttpHelper.getHttpRequestHeader(httpURLConnection);
					LogFileUtil.i(TAG, "doPostAsyn -> initHttpConnection over requestHeader\n" + requestHeader);

					/** 设置参数体 */
					String json = getJsonStr(10, "yline", "1378709");
					initHttpConnectionBody(httpURLConnection, json);

					int responseCode = httpURLConnection.getResponseCode();
					LogFileUtil.i(TAG, "doPostAsyn -> responseCode = " + responseCode);

					if (HTTPCONNECT_SUCCESS_CODE == responseCode) // 连接成功
					{
						bufferedReader =
								new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
						String line = null;
						while (null != (line = bufferedReader.readLine())) // 即:有数据时,一直在读取
						{
							result.append(line);
						}

						/** 响应头,日志 */
						String responseHeader = HttpHelper.getHttpResponseHeader(httpURLConnection);
						LogFileUtil.i(TAG, "doPostAsyn -> responseHeader\n" + responseHeader);

						handleSuccess(callback, result.toString());
					}
					else
					{
						LogFileUtil.e(TAG, "doPostAsyn -> responseCode error");
						handleError(callback, new Exception("doPostAsyn -> responseCode error code = " + responseCode));
					}
				}
				catch (MalformedURLException e)
				{
					LogFileUtil.e(TAG, "doPostAsyn -> ProtocolException", e);
					handleError(callback, e);
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "doPostAsyn -> IOException", e);
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
							LogFileUtil.e(TAG, "doPostAsyn -> close IOException", e);
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
	 * @param connection
	 */
	private static void initHttpConnection(HttpURLConnection connection)
	{
		// 用setRequestProperty方法设置多个自定义的请求头:action，用于后端判断
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("action", "post");

		// 禁用网络缓存
		connection.setUseCaches(false);
		// 定制
		connection.setDoOutput(true); // 设置此方法,允许向服务器输出内容

		// HttpURLConnection默认也支持从服务端读取结果流，因此也可省略
		connection.setDoInput(true);
		connection.setReadTimeout(READ_TIMEOUT); // 设置读取超时为5秒
		connection.setConnectTimeout(CONNNECT_TIMEOUT); // 设置连接网络超时为5秒
	}

	/**
	 * 设置请求体
	 * @param connection
	 * @param json       json字符串
	 */
	private static void initHttpConnectionBody(HttpURLConnection connection, String json)
	{
		OutputStream outputStream = null;
		try
		{
			outputStream = connection.getOutputStream();
			byte[] bytes = getByteFromString(json);
			outputStream.write(bytes);
		}
		catch (IOException e)
		{
			LogFileUtil.e(TAG, "initHttpConnectionBody -> IOException", e);
		}
		finally
		{
			try
			{
				if (null != outputStream)
				{
					outputStream.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回调,抛出异常到主线程
	 * @param callback
	 * @param e
	 */
	private static void handleError(final PostConnectionCallback callback, final Exception e)
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
	 * @param callback
	 * @param result
	 */
	private static void handleSuccess(final PostConnectionCallback callback, final String result)
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

	public interface PostConnectionCallback
	{
		/**
		 * 请求成功,返回结果
		 * @param result
		 */
		void onSuccess(String result);

		/**
		 * 网络错误
		 * @param e
		 */
		void onError(Exception e);
	}

	/**
	 * @param json
	 * @return null if exception
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getByteFromString(String json)
			throws UnsupportedEncodingException
	{
		return json.getBytes("utf-8");
	}

	private static String getJsonStr(int length, String name, String number)
	{
		Gson gson = new Gson();

		ArrayList<PostBean> postBeanList = new ArrayList<PostBean>();
		PostBean postBean;
		for (int i = 0; i < length; i++)
		{
			postBean = new PostBean();
			postBean.setId(i);
			postBean.setName(name + "_" + i);
			postBean.setNumber(number + "_" + i);

			postBeanList.add(postBean);
		}

		return gson.toJson(postBeanList);
	}
}
