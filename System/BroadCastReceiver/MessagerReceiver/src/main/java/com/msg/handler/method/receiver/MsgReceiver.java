package com.msg.handler.method.receiver;

import com.yline.utils.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class MsgReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.v("receiver msg start");
		
		// 描述短信的格式
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		if (null != pdus) {
			for (Object object : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
				String body = smsMessage.getMessageBody(); // 短信内容
				String sender = smsMessage.getOriginatingAddress(); // 短信发送人
				LogUtil.v("sender = " + sender + ",content = " + body);
			}
		} else {
			LogUtil.v("get pdus is null");
		}
        
        /*
        // 因为安全机制,这句话在4.0以后就失效了
        abortBroadcast();
        */
		
		LogUtil.v("receiver msg end");
	}
}
