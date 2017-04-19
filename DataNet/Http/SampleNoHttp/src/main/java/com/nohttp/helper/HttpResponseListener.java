package com.nohttp.helper;

import android.app.Activity;
import android.content.DialogInterface;

import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yline.application.SDKManager;

public class HttpResponseListener<T> implements OnResponseListener<T>
{
	private Activity mActivity;

	/**
	 * Dialog.
	 */
	private WaitDialog mWaitDialog;

	/**
	 * Request.
	 */
	private Request<?> mRequest;

	/**
	 * 结果回调.
	 */
	private HttpListener<T> callback;

	/**
	 * @param activity     context用来实例化dialog.
	 * @param request      请求对象.
	 * @param httpCallback 回调对象.
	 * @param canCancel    是否允许用户取消请求.
	 * @param isLoading    是否显示dialog.
	 */
	public HttpResponseListener(Activity activity, Request<?> request, HttpListener<T> httpCallback, boolean
			canCancel, boolean isLoading)
	{
		this.mActivity = activity;
		this.mRequest = request;
		if (activity != null && isLoading)
		{
			mWaitDialog = new WaitDialog(activity);
			mWaitDialog.setCancelable(canCancel);
			mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialog)
				{
					mRequest.cancel();
				}
			});
		}
		this.callback = httpCallback;
	}

	/**
	 * 开始请求, 这里显示一个dialog.
	 */
	@Override
	public void onStart(int what)
	{
		if (mWaitDialog != null && !mActivity.isFinishing() && !mWaitDialog.isShowing())
		{
			mWaitDialog.show();
		}
	}

	/**
	 * 结束请求, 这里关闭dialog.
	 */
	@Override
	public void onFinish(int what)
	{
		if (mWaitDialog != null && mWaitDialog.isShowing())
		{
			mWaitDialog.dismiss();
		}
	}

	/**
	 * 成功回调.
	 */
	@Override
	public void onSucceed(int what, Response<T> response)
	{
		if (callback != null)
		{
			// 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
			// w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp

			callback.onSucceed(what, response);
		}
	}

	/**
	 * 失败回调.
	 */
	@Override
	public void onFailed(int what, Response<T> response)
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
		if (callback != null)
		{
			callback.onFailed(what, response);
		}
	}
}
