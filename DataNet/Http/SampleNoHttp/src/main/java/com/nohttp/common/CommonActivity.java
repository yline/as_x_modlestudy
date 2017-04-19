package com.nohttp.common;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.nohttp.helper.HttpListener;
import com.nohttp.helper.HttpResponseListener;
import com.nohttp.helper.ImageDialog;
import com.nohttp.helper.WaitDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class CommonActivity extends BaseAppCompatActivity
{
	/**
	 * 用来标记取消。
	 */
	private Object object = new Object();

	/**
	 * 请求队列。
	 */
	protected RequestQueue mQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mWaitDialog = new WaitDialog(this);

		// 初始化请求队列，传入的参数是请求并发值。
		mQueue = NoHttp.newRequestQueue();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		// 和声明周期绑定，退出时取消这个队列中的所有请求，当然可以在你想取消的时候取消也可以，不一定和声明周期绑定。
		mQueue.cancelBySign(object);

		// 因为回调函数持有了activity，所以退出activity时请停止队列。
		mQueue.stop();
	}

	/**
	 * 发起请求。
	 *
	 * @param what      what.
	 * @param request   请求对象。
	 * @param callback  回调函数。
	 * @param canCancel 是否能被用户取消。
	 * @param isLoading 实现显示加载框。
	 * @param <T>       想请求到的数据类型。
	 */
	protected <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading)
	{
		request.setCancelSign(object);
		mQueue.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));
	}

	protected void cancelAll()
	{
		mQueue.cancelAll();
	}

	protected void cancelBySign(Object object)
	{
		mQueue.cancelBySign(object);
	}

	/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Dialog %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/

	/**
	 * 请求的时候等待框。
	 */
	protected WaitDialog mWaitDialog;

	protected void showWaitDialog()
	{
		if (mWaitDialog != null && !mWaitDialog.isShowing())
		{
			mWaitDialog.show();
		}
	}

	protected void dismissWaitDialog()
	{
		if (mWaitDialog != null && mWaitDialog.isShowing())
		{
			mWaitDialog.dismiss();
		}
	}

	protected void showMessageDialog(int title, int messageId)
	{
		showMessageDialog(getText(title), getText(messageId));
	}

	protected void showMessageDialog(int title, CharSequence message)
	{
		showMessageDialog(getText(title), message);
	}

	protected void showMessageDialog(CharSequence title, int messageId)
	{
		showMessageDialog(title, getText(messageId));
	}

	protected void showMessageDialog(CharSequence title, CharSequence message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("知道了", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 显示图片dialog。
	 *
	 * @param title  标题。
	 * @param bitmap 图片。
	 */
	protected void showImageDialog(CharSequence title, Bitmap bitmap)
	{
		ImageDialog imageDialog = new ImageDialog(this);
		imageDialog.setTitle(title);
		imageDialog.setImage(bitmap);
		imageDialog.show();
	}

	protected void handleException(Response<String> response)
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
		LogFileUtil.v(exception.getMessage());
	}
}
