package com.sample.https.connection.get;

import android.os.Handler;

import com.sample.https.activity.IApplication;
import com.sample.https.connection.HttpsHelper;
import com.yline.log.LogFileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * 请求的URL,区分大小写
 * @author YLine
 *         <p/>
 *         2016年7月5日 下午9:20:05
 */
public class GetUtil
{
	private static final String TAG = "connection_get";

	private static final int HTTPCONNECT_SUCCESS_CODE = 200; // 网络请求,正确返回码

	private static final int CONNNECT_TIMEOUT = 5000; // 连接超时时间 ms

	private static final int READ_TIMEOUT = 5000; // 读取超时时间 ms

	private static final String CA_CRT_ASSERT = "CA.crt"; // asserts目录下文件名【区分大小写】

	private static Handler mHandler = new Handler();

	public static void doHttpsLocal(String ip, String projectName, String classStr,
	                                final GetConnectionCallback callback)
	{
		String httpsUrl = String.format("https://%s:443/%s%s", ip, projectName, classStr);
		doGetHttpsAsyn(httpsUrl, callback);
	}

	/**
	 * Get 异步请求
	 * @param httpUrl  URL
	 * @param callback 网络请求回调
	 */
	private static void doGetHttpsAsyn(final String httpsUrl, final GetConnectionCallback callback)
	{
		LogFileUtil.v(TAG, "doGetHttpsAsyn -> in -> httpsUrl:" + httpsUrl);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				URL url = null;
				HttpsURLConnection httpsURLConnection = null;

				BufferedReader bufferedReader = null;

				StringBuffer result = new StringBuffer();

				try
				{
					url = new URL(httpsUrl); // 创建一个URL对象
					LogFileUtil.v(TAG, "doGetHttpsAsyn -> new url over");

					// 调用URL的openConnection()方法,获取HttpsURLConnection对象
					httpsURLConnection = (HttpsURLConnection) url.openConnection();

					// HttpsURLConnection默认就是用GET发送请求,因此也可省略
					httpsURLConnection.setRequestMethod("GET");
					initHttpsConnection(httpsURLConnection);
					/** 请求头,日志 */
					String requestHeader = HttpsHelper.getHttpsRequestHeader(httpsURLConnection);
					LogFileUtil.i(TAG, "doGetHttpsAsyn -> initHttpsConnection over requestHeader\n" + requestHeader);

					int responseCode = httpsURLConnection.getResponseCode();
					LogFileUtil.i(TAG, "doGetHttpsAsyn -> responseCode = " + responseCode);

					if (HTTPCONNECT_SUCCESS_CODE == responseCode) // 连接成功
					{
						bufferedReader =
								new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), "utf-8"));
						String line = null;
						while (null != (line = bufferedReader.readLine())) // 即:有数据时,一直在读取
						{
							result.append(line);
						}

						/** 响应头,日志 */
						String responseHeader = HttpsHelper.getHttpsResponseHeader(httpsURLConnection);
						LogFileUtil.i(TAG, "doGetHttpsAsyn -> responseHeader\n" + responseHeader);

						handleSuccess(callback, result.toString());
					}
					else
					{
						LogFileUtil.e(TAG, "doGetHttpsAsyn -> responseCode error");
						handleError(callback,
								new Exception("doGetHttpsAsyn -> responseCode error code = " + responseCode));
					}
				}
				catch (MalformedURLException e)
				{
					LogFileUtil.e(TAG, "doGetHttpsAsyn -> MalformedURLException", e);
					handleError(callback, e);
				}
				catch (ProtocolException e)
				{
					LogFileUtil.e(TAG, "doGetHttpsAsyn -> ProtocolException", e);
					handleError(callback, e);
				}
				catch (IOException e)
				{
					LogFileUtil.e(TAG, "doGetHttpsAsyn -> IOException", e);
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
							LogFileUtil.e(TAG, "doGetHttpsAsyn -> close IOException", e);
							handleError(callback, e);
						}
					}
					if (null != httpsURLConnection)
					{
						httpsURLConnection.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * https 链接  设置
	 * RequestHeader : setRequestProperty
	 * parameter     : 直接在后面加的参数
	 * @param httpsURLConnection
	 */
	private static void initHttpsConnection(HttpsURLConnection httpsURLConnection)
	{
		// 用setRequestProperty方法设置多个自定义的请求头:action，用于后端判断
		httpsURLConnection.setRequestProperty("accept", "*/*");
		httpsURLConnection.setRequestProperty("connection", "Keep-Alive");
		httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpsURLConnection.setRequestProperty("charset", "utf-8");
		httpsURLConnection.setRequestProperty("action", "get");

		// 禁用网络缓存
		httpsURLConnection.setUseCaches(false);
		// 定制,只要设置了下面这句话,系统就会自动改为POST请求..因此,必须注释掉
		// connection.setDoOutput(true); // 设置此方法,允许向服务器输出内容

		// HttpURLConnection默认也支持从服务端读取结果流，因此也可省略
		httpsURLConnection.setDoInput(true);
		httpsURLConnection.setReadTimeout(READ_TIMEOUT); // 设置读取超时为5秒
		httpsURLConnection.setConnectTimeout(CONNNECT_TIMEOUT); // 设置连接网络超时为5秒

		LogFileUtil.v(TAG, "initHttpsConnection -> https set start");
		// https
		try
		{
			httpsURLConnection.setSSLSocketFactory(getSSLContext().getSocketFactory());
			httpsURLConnection.setHostnameVerifier(new HostnameVerifier()
			{

				@Override
				public boolean verify(String hostname, SSLSession session)
				{
					// 进行证书校验,本地默认 认证通过校验
					return true;
				}
			});
		}
		catch (Exception e)
		{
			LogFileUtil.e(TAG, "initHttpsConnection -> getSSLContext -> Exception", e);
		}
		LogFileUtil.v(TAG, "initHttpsConnection -> https set end");
	}

	/**
	 * 证书认证
	 * @return SSLContext or exception
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLContext getSSLContext()
			throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException
	{
		// 从asserts目录中获取CA.cer证书的文件流
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream in = IApplication.getApplication().getAssets().open(CA_CRT_ASSERT); // 区分大小写

		// 将该文件流转化为一个证书对象Certificate
		Certificate ca = cf.generateCertificate(in);

		// 创建一个默认类型的KeyStore实例，keyStore用于存储着我们信赖的数字证书
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(null, null);
		keystore.setCertificateEntry("CA", ca);

		// 获取一个默认的TrustManagerFactory的实例，并用之前的keyStore初始化它
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		tmf.init(keystore);

		// 创建一个TLS类型的SSLContext实例，并用之前的TrustManager数组初始化sslContext实例，这样该sslContext实例也会信任CA.cer证书
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), null);

		return sslContext;
	}

	/**
	 * 回调,抛出异常到主线程
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
		 * @param result
		 */
		void onSuccess(String result);

		/**
		 * 网络错误
		 * @param e
		 */
		void onError(Exception e);
	}
}
