package com.system.app.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/10/6.
 */
public class MessageReceiver extends BroadcastReceiver
{
	private static final String TAG = "MessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		Object[] pdus = (Object[]) bundle.get("pdus"); // 提取短信消息
		SmsMessage[] messages = new SmsMessage[pdus.length];
		for (int i = 0; i < messages.length; i++)
		{
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		}
		String address = messages[0].getOriginatingAddress(); // 获取发送方 号码
		String fullMessage = "";
		for (SmsMessage message : messages)
		{
			fullMessage += message.getMessageBody(); // 获取短信内容
		}

		LogFileUtil.v(TAG, "onReceiver full message = " + fullMessage);
	}
}
