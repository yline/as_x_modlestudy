package com.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;

import com.alarm.custom.CustomActivity;
import com.yline.test.BaseTestActivity;

/**
 * @author yline 2016/11/26 --> 12:33
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		// 自定义闹钟
		addButton("custom alarm clock", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomActivity.launch(MainActivity.this);
			}
		});
		
		// 调用系统闹钟
		addButton("system alarm clock", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent alarmsIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
				startActivity(alarmsIntent);
			}
		});
	}
}
