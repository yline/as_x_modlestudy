package com.threshold.local.receiver;

import android.content.Context;
import android.content.Intent;

import com.threshold.local.activity.MainApplication;
import com.yline.base.BaseReceiver;
import com.yline.log.LogFileUtil;

/**
 * 本地广播特点：
 * 1,只能动态注册
 * 2,无法接收其它广播的消息		(单通道)
 * 3,无法发送给其他广播消息		(单通道)
 * @author yline 2016/10/26 --> 7:56
 * @version 1.0.0
 */
public class TestReceiver extends BaseReceiver
{
	public static final String ACTION_ONE = "yline.action.one";

	public static final String ACTION_TWO = "yline.action.two";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		if (null != intent)
		{
			String action = intent.getAction();
			if (null != action)
			{
				LogFileUtil.v("action = " + action);
				MainApplication.toast("action = " + action);
			}
			else
			{
				LogFileUtil.v("action is null");
				MainApplication.toast("action is null");
			}
		}
		else
		{
			LogFileUtil.v("intent is null");
			MainApplication.toast("intent is null");
		}
	}
}
