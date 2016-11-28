package com.alarm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alarm.R;
import com.alarm.clock.AlarmHelper;
import com.alarm.view.RemindCyclePopup;
import com.alarm.view.RemindWayPopup;
import com.yline.base.BaseAppCompatActivity;

import java.util.List;

public class CustomActivity extends BaseAppCompatActivity
{
	private TimePicker timePicker;

	private RelativeLayout rlRepeat, rlRing;

	private TextView tv_repeat_value, tv_ring_value;

	private LinearLayout allLayout;

	private TextView tvDate;

	private Button btnSet;

	private int hour, min;

	private RemindWayPopup.REMIND_WAY remindWay;

	private List<RemindCyclePopup.REMIND_CYCLE> remindCycleList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom);

		initView();

		timePicker = (TimePicker) findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
		{
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
			{
				tvDate.setText(hourOfDay + ":" + minute);
				hour = hourOfDay;
				min = minute;
			}
		});
	}

	private void initView()
	{
		allLayout = (LinearLayout) findViewById(R.id.all_layout);
		btnSet = (Button) findViewById(R.id.set_btn);
		btnSet.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				timePicker.setVisibility(View.GONE);
				setClock();
			}
		});

		tvDate = (TextView) findViewById(R.id.date_tv);
		tvDate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (View.GONE == timePicker.getVisibility())
				{
					timePicker.setVisibility(View.VISIBLE);
				}
			}
		});

		rlRepeat = (RelativeLayout) findViewById(R.id.repeat_rl);
		rlRepeat.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selectRemindCycle();
			}
		});

		rlRing = (RelativeLayout) findViewById(R.id.ring_rl);
		rlRing.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selectRingWay();
			}
		});

		tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
		tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
	}

	private void setClock()
	{
		new AlarmHelper.Builder().setHour(hour).setMinute(min).setRepeat(false).create(this);
	}

	public void selectRemindCycle()
	{
		RemindCyclePopup remindCyclePopup = new RemindCyclePopup(this);
		remindCyclePopup.showPopup(allLayout);
		remindCyclePopup.setOnRemindCycleListener(new RemindCyclePopup.OnRemindCycleListener()
		{
			@Override
			public void onResult(List<RemindCyclePopup.REMIND_CYCLE> cycleList)
			{
				remindCycleList = cycleList;
				tv_repeat_value.setText(cycleList.toString());
			}
		});
	}

	private void selectRingWay()
	{
		RemindWayPopup remindWayPopup = new RemindWayPopup(this);
		remindWayPopup.showPopup(allLayout);
		remindWayPopup.setOnRemindWayListener(new RemindWayPopup.OnRemindWayListener()
		{
			@Override
			public void onResult(RemindWayPopup.REMIND_WAY way)
			{
				remindWay = way;
				tv_ring_value.setText(way.getCn());
			}
		});
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, CustomActivity.class));
	}
}
