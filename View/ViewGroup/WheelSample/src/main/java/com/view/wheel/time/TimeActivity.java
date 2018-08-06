package com.view.wheel.time;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.view.wheel.R;
import com.view.wheel.time.view.WheelDatePicker;
import com.yline.base.BaseAppCompatActivity;

import java.util.Date;

/**
 * 时间轮播器
 *
 * @author yline 2018/3/5 -- 14:57
 * @version 1.0.0
 */
public class TimeActivity extends BaseAppCompatActivity {
	public static void launcher(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, TimeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		
		WheelDatePicker datePicker = findViewById(R.id.time_wheel_picker);
		datePicker.setOnDateSelectedListener(new WheelDatePicker.OnDateSelectedListener() {
			@Override
			public void onDateSelected(WheelDatePicker picker, Date date) {
				// TODO
			}
		});
	}
}
