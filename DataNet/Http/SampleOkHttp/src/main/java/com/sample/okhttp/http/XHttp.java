package com.sample.okhttp.http;

import com.google.gson.Gson;
import com.sample.okhttp.httphelper.HttpDefaultClient;
import com.sample.okhttp.httphelper.HttpHandler;
import com.sample.okhttp.httphelper.IHttpResponse;
import com.yline.log.LogFileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 实现Http请求管理,并进行Http请求
 *
 * @author yline 2017/3/9 --> 13:14
 * @version 1.0.0
 */
public abstract class XHttp<Result> implements IHttpResponse<Result>
{
	public static final boolean isDebug = true;

	public static final int REQUEST_POST = 0;

	public static final int REQUEST_GET = 1;

	private static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";

	private static final int REQUEST_SUCCESS_CODE = 0;

	private HttpHandler httpHandler;

	public XHttp()
	{
		if (isResponseHandler())
		{
			httpHandler = HttpHandler.build();
		}
	}

	/**
	 * 所有的网络请求
	 *
	 * @param actionUrl
	 * @param clazz
	 */
	public void doRequest(String actionUrl, final Class<Result> clazz)
	{
		// 配置Client
		OkHttpClient okHttpClient = getClient();

		// 配置请求参数
		Request.Builder builder = getRequestBuilder();
		// 1,cache
		builder.cacheControl(getCacheBuilder());

		// 2,get、post区分
		if (REQUEST_POST == getRequestType())
		{
			String postHttpUrl = getRequestUrlBase() + actionUrl;
			if (isDebug())
			{
				LogFileUtil.v("postHttpUrl", postHttpUrl);
			}

			builder.url(postHttpUrl).post(getPostRequestBody());
		}
		else if (REQUEST_GET == getRequestType())
		{
			String getHttpUrl = String.format("%s%s?%s", getRequestUrlBase(), actionUrl, getGetParamUrl());
			if (isDebug())
			{
				LogFileUtil.v("getHttpUrl", getHttpUrl);
			}

			builder.url(getHttpUrl);
		}

		// 建立请求
		Request request = builder.build();

		// 发送请求
		okHttpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, final IOException e)
			{
				handleFailure(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				// 先进行code处理一次
				String jsonResult;
				if (isResponseCodeHandler())
				{
					String data = response.body().string();
					try
					{
						JSONObject jsonObject = new JSONObject(data);

						int code = jsonObject.getInt("code");
						if (getResponseDefaultCode() == code)
						{
							jsonResult = jsonObject.getString("data");
						}
						else
						{
							handleFailureCode(code);
							return;
						}
					} catch (JSONException ex)
					{
						handleFailure(ex);
						return;
					}
				}
				else
				{
					jsonResult = response.body().string();
				}

				// response.body().close();

				if (isDebug())
				{
					LogFileUtil.v("onSuccess", (null == jsonResult ? "null" : jsonResult.toString()));
				}

				if (isResponseParse())
				{
					// 解析
					Result result = new Gson().fromJson(jsonResult, clazz);
					handleSuccess(result);
				}
				else
				{
					handleSuccess((Result) jsonResult);
				}
			}
		});
	}

	private void handleSuccess(final Result result)
	{
		if (isResponseHandler())
		{
			httpHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					XHttp.this.onSuccess(result);
				}
			});
		}
		else
		{
			XHttp.this.onSuccess(result);
		}
	}

	private void handleFailureCode(final int code)
	{
		if (isResponseHandler())
		{
			httpHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					XHttp.this.onFailureCode(code);
				}
			});
		}
		else
		{
			XHttp.this.onFailureCode(code);
		}
	}

	private void handleFailure(final Exception ex)
	{
		// 是否需要 Handler 处理一次
		if (isResponseHandler())
		{
			httpHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					XHttp.this.onFailure(ex);
				}
			});
		}
		else
		{
			XHttp.this.onFailure(ex);
		}
	}

	private RequestBody getPostRequestBody()
	{
		String jsonBody = new Gson().toJson(getRequestPostParam());
		if (isDebug())
		{
			LogFileUtil.v("requestBody", "jsonBody = " + jsonBody);
		}
		return RequestBody.create(getRequestPostMediaType(), jsonBody);
	}

	private String getGetParamUrl()
	{
		Map<String, String> map = getRequestGetParam();
		if (null == map)
		{
			return "";
		}
		else
		{
			StringBuffer stringBuffer = new StringBuffer();
			boolean isFirst = true;
			for (String key : map.keySet())
			{
				if (isFirst)
				{
					isFirst = false;
				}
				else
				{
					stringBuffer.append("&");
				}
				stringBuffer.append(key);
				stringBuffer.append("=");
				stringBuffer.append(map.get(key));
			}
			return stringBuffer.toString();
		}
	}

	public abstract void onSuccess(Result result);

	@Override
	public void onFailureCode(int code)
	{
		if (isDebug())
		{
			LogFileUtil.e("onFailureCode", "code = " + code);
		}
	}

	@Override
	public void onFailure(Exception ex)
	{
		if (isDebug())
		{
			LogFileUtil.e("onFailure", "net exception happened", ex);
		}
	}

	public static boolean isDebug()
	{
		return true;
	}

	 /* --------------------------- 以下代码用来设置参数 --------------------------- */

	/**
	 * 默认 单例方式获取 HttpDefaultClient
	 *
	 * @return
	 */
	protected OkHttpClient getClient()
	{
		return HttpDefaultClient.getInstance();
	}

	/**
	 * 默认不设置的一些配置,可以让用户自定义配置；例如Header
	 * 但是如果有该文件内已经配置好的,则已配置的优先
	 *
	 * @return
	 */
	protected Request.Builder getRequestBuilder()
	{
		return new Request.Builder();
	}

	/**
	 * 设置请求方式
	 *
	 * @return
	 */
	protected int getRequestType()
	{
		return REQUEST_POST;
	}

	/**
	 * 设置请求,头Url:例如
	 * http://120.92.35.211/wanghong/wh/index.php/Back/Api/:
	 *
	 * @return
	 */
	protected String getRequestUrlBase()
	{
		return "http://120.92.35.211/wanghong/wh/index.php/Back/Api/";
	}

	/**
	 * Post请求数据内容;Json
	 *
	 * @return
	 */
	protected Object getRequestPostParam()
	{
		return "";
	}

	protected MediaType getRequestPostMediaType()
	{
		return MediaType.parse(MEDIA_TYPE_JSON);
	}

	/**
	 * 获取Get请求后缀
	 *
	 * @return
	 */
	protected Map<String, String> getRequestGetParam()
	{
		return null;
	}

	/**
	 * 是否需要 Handler 处理一次
	 *
	 * @return
	 */
	protected boolean isResponseHandler()
	{
		return true;
	}

	/**
	 * 是否先进行一次Code解析
	 *
	 * @return
	 */
	protected boolean isResponseCodeHandler()
	{
		return true;
	}

	/**
	 * 如果返回的,以字符串输出；则设置成true
	 * 如果需要解析,则设置成false
	 *
	 * @return
	 */
	protected boolean isResponseParse()
	{
		return true;
	}

	/**
	 * 默认正确的Code
	 *
	 * @return
	 */
	protected int getResponseDefaultCode()
	{
		return REQUEST_SUCCESS_CODE;
	}

	/**
	 * 配置缓存选项
	 *
	 * @return
	 */
	protected CacheControl getCacheBuilder()
	{
		final CacheControl.Builder cacheBuilder = new CacheControl.Builder();
		// cacheBuilder.noCache(); // 不使用缓存，用网络请求
		// cacheBuilder.noStore(); // 不使用缓存，也不存储缓存
		// cacheBuilder.onlyIfCached(); // 只使用缓存
		// cacheBuilder.noTransform(); // 禁止转码
		cacheBuilder.maxAge(1000, TimeUnit.SECONDS); // 本地能够使用这个数据多久
		cacheBuilder.maxStale(300, TimeUnit.SECONDS); // 如果超过这个时间,则认为数据过时，从新请求；如果没超过这个时间，则不会发送请求
		cacheBuilder.minFresh(100, TimeUnit.SECONDS); // 超时时间为当前时间加上10秒钟。
		return cacheBuilder.build();
	}
	

}
