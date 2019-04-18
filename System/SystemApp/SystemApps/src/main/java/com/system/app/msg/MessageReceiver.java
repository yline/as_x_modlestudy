package com.system.app.msg;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

/**
 * 权限：android.permission.RECEIVE_SMS
 * Created by yline on 2016/10/6.
 */
public class MessageReceiver extends BroadcastReceiver {
    public static MessageReceiver register(Context context) {
        MessageReceiver messageReceiver = new MessageReceiver();

        IntentFilter filter = new IntentFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        } else {
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        }
        context.registerReceiver(messageReceiver, filter);
        return messageReceiver;
    }

    public static void unregister(Context context, MessageReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    private OnMessageCallback mOnMessageCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.v("");

        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            Object[] pdusArray = (Object[]) bundle.get("pdus");
            String result = readMessage(pdusArray);
            LogUtil.v("result = " + result);

            if (null != mOnMessageCallback) {
                mOnMessageCallback.onReceive(result);
            }
        }
    }

    public void setOnMessageCallback(OnMessageCallback callback) {
        this.mOnMessageCallback = callback;
    }

    public interface OnMessageCallback {
        /**
         * 短信内容
         *
         * @param msg 短信内容
         */
        void onReceive(String msg);
    }

    /**
     * 读取到相关短信
     *
     * @param pdusArray 内容
     * @return 内容
     */
    private String readMessage(Object[] pdusArray) {
        StringBuilder sBuilder = new StringBuilder();
        if (null != pdusArray) {
            for (Object pdu : pdusArray) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                sBuilder.append((null == smsMessage ? "" : smsMessage.getMessageBody()));
            }
        }
        return sBuilder.toString();
    }
}
