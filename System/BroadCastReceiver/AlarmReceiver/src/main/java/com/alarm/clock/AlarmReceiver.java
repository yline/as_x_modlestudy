package com.alarm.clock;

import android.content.Context;
import android.content.Intent;

import com.alarm.activity.MainApplication;
import com.yline.base.BaseReceiver;

/**
 * 能够准时的收到定下的闹钟
 * @author yline 2016/11/28 --> 21:22
 * @version 1.0.0
 */
public class AlarmReceiver extends BaseReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		AlarmHelper.getInstance().setAlarm(context, intent);
		MainApplication.toast("成功接收到系统闹钟广播");
	}
}
