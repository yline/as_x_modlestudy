package com.threshold.local.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.threshold.local.MainApplication;
import com.yline.base.BaseReceiver;
import com.yline.utils.LogUtil;

/**
 * 本地广播特点：
 * 1,只能动态注册
 * 2,无法接收其它广播的消息		(单通道)
 * 3,无法发送给其他广播消息		(单通道)
 *
 * @author yline 2016/10/26 --> 7:56
 * @version 1.0.0
 */
public class TestReceiver extends BaseReceiver {
	public static final String ACTION_ONE = "yline.action.one";
	
	public static final String ACTION_TWO = "yline.action.two";
	
	public static TestReceiver register(Context context) {
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TestReceiver.ACTION_ONE);
		intentFilter.addAction(TestReceiver.ACTION_TWO);
		
		TestReceiver localReceiver = new TestReceiver();
		localBroadcastManager.registerReceiver(localReceiver, intentFilter);
		return localReceiver;
	}
	
	public static void unRegister(Context context, TestReceiver localReceiver) {
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
		localBroadcastManager.unregisterReceiver(localReceiver);
	}
	
	public static void sendActionOne(Context context) {
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
		localBroadcastManager.sendBroadcast(new Intent(TestReceiver.ACTION_ONE));
	}
	
	public static void sendActionTwo(Context context) {
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
		localBroadcastManager.sendBroadcast(new Intent(TestReceiver.ACTION_TWO));
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (null != intent) {
			String action = intent.getAction();
			if (null != action) {
				LogUtil.v("action = " + action);
				MainApplication.toast("action = " + action);
			} else {
				LogUtil.v("action is null");
				MainApplication.toast("action is null");
			}
		} else {
			LogUtil.v("intent is null");
			MainApplication.toast("intent is null");
		}
	}
}
