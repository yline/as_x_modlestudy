package com.alarm.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alarm.R;
import com.alarm.receiver.AlarmReceiver;
import com.alarm.custom.view.RemindCyclePopup;
import com.alarm.custom.view.RemindWayPopup;
import com.yline.base.BaseAppCompatActivity;

import java.util.List;

public class CustomActivity extends BaseAppCompatActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, CustomActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private TimePicker timePicker;
	private TextView tv_repeat_value, tv_ring_value;
	private LinearLayout allLayout;
	private TextView tvDate;
	
	private int hour, min;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom);
		
		initView();
		
		timePicker = (TimePicker) findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				tvDate.setText(hourOfDay + ":" + minute);
				hour = hourOfDay;
				min = minute;
			}
		});
	}
	
	private void initView() {
		allLayout = findViewById(R.id.all_layout);
		tvDate = findViewById(R.id.date_tv);
		tv_repeat_value = findViewById(R.id.tv_repeat_value);
		tv_ring_value = findViewById(R.id.tv_ring_value);
		
		initViewClick();
	}
	
	private void initViewClick() {
		findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				timePicker.setVisibility(View.GONE);
				AlarmReceiver.setClock(CustomActivity.this, hour, min, false, 0);
			}
		});
		
		findViewById(R.id.repeat_rl).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectRemindCycle();
			}
		});
		
		findViewById(R.id.ring_rl).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectRingWay();
			}
		});
		
		tvDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (View.GONE == timePicker.getVisibility()) {
					timePicker.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public void selectRemindCycle() {
		RemindCyclePopup remindCyclePopup = new RemindCyclePopup(this);
		remindCyclePopup.showPopup(allLayout);
		remindCyclePopup.setOnRemindCycleListener(new RemindCyclePopup.OnRemindCycleListener() {
			@Override
			public void onResult(List<RemindCyclePopup.REMIND_CYCLE> cycleList) {
				tv_repeat_value.setText(cycleList.toString());
			}
		});
	}
	
	private void selectRingWay() {
		RemindWayPopup remindWayPopup = new RemindWayPopup(this);
		remindWayPopup.showPopup(allLayout);
		remindWayPopup.setOnRemindWayListener(new RemindWayPopup.OnRemindWayListener() {
			@Override
			public void onResult(RemindWayPopup.REMIND_WAY way) {
				tv_ring_value.setText(way.getCn());
			}
		});
	}
}
