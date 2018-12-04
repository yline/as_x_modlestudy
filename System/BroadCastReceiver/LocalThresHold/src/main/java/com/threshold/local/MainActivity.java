package com.threshold.local;

import android.os.Bundle;
import android.view.View;

import com.threshold.local.receiver.TestReceiver;
import com.yline.test.BaseTestActivity;
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
public class MainActivity extends BaseTestActivity {
	// 管理器
	private TestReceiver mLocalReceiver;
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		mLocalReceiver = TestReceiver.register(MainActivity.this);
		
		addButton("发送本地广播 1", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.v("btn_send_local_one");
				
				TestReceiver.sendActionOne(MainActivity.this);
			}
		});
		
		addButton("发送本地广播 2", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.v("btn_send_local_two");
				
				TestReceiver.sendActionTwo(MainActivity.this);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		TestReceiver.unRegister(this, mLocalReceiver);
	}
	

}
