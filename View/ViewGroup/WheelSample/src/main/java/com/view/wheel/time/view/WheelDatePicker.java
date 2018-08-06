package com.view.wheel.time.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.view.wheel.R;
import com.wheel.lib.IDebug;
import com.wheel.lib.WheelPicker;
import com.yline.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WheelDatePicker extends LinearLayout implements IDebug {
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
	
	private WheelYearPicker mPickerYear;
	private WheelMonthPicker mPickerMonth;
	private WheelDayPicker mPickerDay;
	
	private OnDateSelectedListener mListener;
	
	private int mYear, mMonth, mDay;
	
	public WheelDatePicker(Context context) {
		this(context, null);
	}
	
	public WheelDatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.view_wheel_date_picker, this);
		
		mPickerYear = (WheelYearPicker) findViewById(R.id.wheel_date_picker_year);
		mPickerMonth = (WheelMonthPicker) findViewById(R.id.wheel_date_picker_month);
		mPickerDay = (WheelDayPicker) findViewById(R.id.wheel_date_picker_day);
		
		setMaximumWidthTextYear();
		mPickerMonth.setMaximumWidthText("00");
		mPickerDay.setMaximumWidthText("00");
		
		mYear = mPickerYear.getCurrentYear();
		mMonth = mPickerMonth.getCurrentMonth();
		mDay = mPickerDay.getCurrentDay();
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 年
		mPickerYear.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener<Integer>() {
			@Override
			public void onItemSelected(WheelPicker picker, Integer year, int position) {
				mYear = year;
				mPickerDay.setYear(year);
				callDataSelectedListener();
			}
		});
		
		// 月
		mPickerMonth.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener<Integer>() {
			@Override
			public void onItemSelected(WheelPicker picker, Integer month, int position) {
				mMonth = month;
				mPickerDay.setMonth(month);
				callDataSelectedListener();
			}
		});
		
		// 日
		mPickerDay.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener<Integer>() {
			@Override
			public void onItemSelected(WheelPicker picker, Integer day, int position) {
				mDay = day;
				callDataSelectedListener();
			}
		});
	}
	
	private void setMaximumWidthTextYear() {
		List years = mPickerYear.getData();
		String lastYear = String.valueOf(years.get(years.size() - 1));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lastYear.length(); i++) {
			sb.append("0");
		}
		mPickerYear.setMaximumWidthText(sb.toString());
	}
	
	public void callDataSelectedListener() {
		if (null != mListener) {
			try {
				String date = mYear + "-" + mMonth + "-" + mDay;
				LogUtil.v("date = " + date);
				mListener.onDateSelected(this, SDF.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void setDebug(boolean isDebug) {
		mPickerYear.setDebug(isDebug);
		mPickerMonth.setDebug(isDebug);
		mPickerDay.setDebug(isDebug);
	}
	
	public void setOnDateSelectedListener(OnDateSelectedListener listener) {
		mListener = listener;
	}
	
	public interface OnDateSelectedListener {
		void onDateSelected(WheelDatePicker picker, Date date);
	}
}