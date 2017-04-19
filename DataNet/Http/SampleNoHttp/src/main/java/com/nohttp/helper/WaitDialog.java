package com.nohttp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * 等待对话框
 *
 * @author yline 2017/4/19 -- 9:53
 * @version 1.0.0
 */
public class WaitDialog extends ProgressDialog
{
	public WaitDialog(Context context)
	{
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setProgressStyle(STYLE_SPINNER);
		setMessage("正在请求，请稍后…");
	}
}
