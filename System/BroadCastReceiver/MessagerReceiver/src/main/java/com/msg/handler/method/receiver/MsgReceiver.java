package com.msg.handler.method.receiver;

import com.msg.handler.method.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class MsgReceiver extends BroadcastReceiver
{
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        LogFileUtil.v(MainApplication.TAG, "receiver msg start");
        
        // 描述短信的格式
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        if (null != pdus)
        {
            for (Object object : pdus)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
                String body = smsMessage.getMessageBody(); // 短信内容
                String sender = smsMessage.getOriginatingAddress(); // 短信发送人
                LogFileUtil.v(MainApplication.TAG, "sender = " + sender + ",content = " + body);
            }
        }
        else
        {
            LogFileUtil.v(MainApplication.TAG, "get pdus is null");
        }
        
        /*
        // 因为安全机制,这句话在4.0以后就失效了
        abortBroadcast();
        */
        
        LogFileUtil.v(MainApplication.TAG, "receiver msg end");
    }
}
