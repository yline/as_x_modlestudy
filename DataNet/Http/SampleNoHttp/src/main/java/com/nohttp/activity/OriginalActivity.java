package com.nohttp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nohttp.R;
import com.nohttp.common.CommonActivity;
import com.nohttp.common.Constants;
import com.nohttp.common.WaitDialog;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yline.application.SDKManager;

import java.util.Locale;

public class OriginalActivity extends CommonActivity
{
	/**
	 * 用来标志请求的what, 类似handler的what一样，这里用来区分请求。
	 */
	private static final int NOHTTP_WHAT_TEST = 0x001;
	
	/**
	 * 请求的时候等待框。
	 */
	private WaitDialog mWaitDialog;
	
	private Object sign = new Object();
	
	private TextView mTvResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_original);
		
		mWaitDialog = new WaitDialog(this);
		mTvResult = (TextView) findViewById(R.id.tv_result);
		
		findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 创建请求对象。
				Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_JSONOBJECT, RequestMethod.GET);
				
				
				// 添加请求参数。
				request.add("name", "yanzhenjie") // String型。
						.add("pwd", 123) // int型。
						.add("userAge", 1.25) // double型。
						.add("nooxxx", 1.2F) // flocat型。
						
						// 单个请求的超时时间，不指定就会使用全局配置。
						.setConnectTimeout(10 * 1000) // 设置连接超时。
						.setReadTimeout(20 * 1000) // 设置读取超时时间，也就是服务器的响应超时。
						
						// 请求头，是否要添加头，添加什么头，要看开发者服务器端的要求。
						.addHeader("Author", "sample")
						.setHeader("User", "Jason")
						
						// 设置一个tag, 在请求完(失败/成功)时原封不动返回; 多数情况下不需要。
						.setTag(new Object())
						// 设置取消标志。
						.setCancelSign(sign);

				/*
		 * what: 当多个请求同时使用同一个OnResponseListener时用来区分请求, 类似handler的what一样。
		 * request: 请求对象。
		 * onResponseListener 回调对象，接受请求结果。
		 */
				mQueue.add(NOHTTP_WHAT_TEST, request, onResponseListener);
			}
		});
	}
	
	/**
	 * 回调对象，接受请求结果.
	 */
	private OnResponseListener<String> onResponseListener = new OnResponseListener<String>()
	{
		@Override
		public void onSucceed(int what, Response<String> response)
		{
			if (what == NOHTTP_WHAT_TEST)
			{// 根据what判断是哪个请求的返回，这样就可以用一个OnResponseListener来接受多个请求的结果。
				int responseCode = response.getHeaders().getResponseCode();// 服务器响应码。
				
				String result = response.get();// 响应结果。
				
				mTvResult.setText(result);
				
				Object tag = response.getTag();// 拿到请求时设置的tag。
				
				// 响应头
				Headers headers = response.getHeaders();
				String headResult = "响应码：%1$d\\n花费时间：%2$d毫秒。";
				headResult = String.format(Locale.getDefault(), headResult, headers.getResponseCode(), response.getNetworkMillis());
				mTvResult.setText(headResult);
			}
		}
		
		@Override
		public void onStart(int what)
		{
			// 请求开始，这里可以显示一个dialog
			if (mWaitDialog != null && !mWaitDialog.isShowing())
			{
				mWaitDialog.show();
			}
		}
		
		@Override
		public void onFinish(int what)
		{
			// 请求结束，这里关闭dialog
			if (mWaitDialog != null && mWaitDialog.isShowing())
			{
				mWaitDialog.dismiss();
			}
		}
		
		@Override
		public void onFailed(int what, Response<String> response)
		{
			Exception exception = response.getException();
			if (exception instanceof NetworkError)
			{// 网络不好
				SDKManager.toast("请检查网络。");
			}
			else if (exception instanceof TimeoutError)
			{// 请求超时
				SDKManager.toast("请求超时，网络不好或者服务器不稳定。");
			}
			else if (exception instanceof UnKnownHostError)
			{// 找不到服务器
				SDKManager.toast("未发现指定服务器，清切换网络后重试。");
			}
			else if (exception instanceof URLError)
			{// URL是错的
				SDKManager.toast("URL错误。");
			}
			else if (exception instanceof NotFoundCacheError)
			{
				// 这个异常只会在仅仅查找缓存时没有找到缓存时返回
				SDKManager.toast("没有找到缓存.");
			}
			else
			{
				SDKManager.toast("未知错误。");
			}
			Logger.e("错误：" + exception.getMessage());
		}
	};
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, OriginalActivity.class));
	}
}
