package com.nohttp.common;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yline.base.BaseAppCompatActivity;

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
	public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading)
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

	/**
	 * Show message dialog.
	 *
	 * @param title   title.
	 * @param message message.
	 */
	public void showMessageDialog(int title, int message)
	{
		showMessageDialog(getText(title), getText(message));
	}

	/**
	 * Show message dialog.
	 *
	 * @param title   title.
	 * @param message message.
	 */
	public void showMessageDialog(int title, CharSequence message)
	{
		showMessageDialog(getText(title), message);
	}

	/**
	 * Show message dialog.
	 *
	 * @param title   title.
	 * @param message message.
	 */
	public void showMessageDialog(CharSequence title, int message)
	{
		showMessageDialog(title, getText(message));
	}

	/**
	 * Show message dialog.
	 *
	 * @param title   title.
	 * @param message message.
	 */
	public void showMessageDialog(CharSequence title, CharSequence message)
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
	public void showImageDialog(CharSequence title, Bitmap bitmap)
	{
		ImageDialog imageDialog = new ImageDialog(this);
		imageDialog.setTitle(title);
		imageDialog.setImage(bitmap);
		imageDialog.show();
	}
}
