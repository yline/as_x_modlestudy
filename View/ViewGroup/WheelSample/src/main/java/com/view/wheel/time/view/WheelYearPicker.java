package com.view.wheel.time.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wheel.lib.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 年份选择器
 * <p/>
 * Picker for Years
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelYearPicker extends WheelPicker {
	private int mYearStart = 1000, mYearEnd = 3000;
	
	private int mSelectedYear;
	
	public WheelYearPicker(Context context) {
		this(context, null);
	}
	
	public WheelYearPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		updateYears();
		mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
		updateSelectedYear();
	}
	
	private void updateYears() {
		List<Integer> data = new ArrayList<>();
		for (int i = mYearStart; i <= mYearEnd; i++) {
			data.add(i);
		}
		super.setData(data);
	}
	
	private void updateSelectedYear() {
		setSelectedItemPosition(mSelectedYear - mYearStart);
	}
	
	public void setYearFrame(int start, int end) {
		mYearStart = start;
		mYearEnd = end;
		mSelectedYear = getCurrentYear();
		updateYears();
		updateSelectedYear();
	}
	
	public int getYearStart() {
		return mYearStart;
	}
	
	public void setYearStart(int start) {
		mYearStart = start;
		mSelectedYear = getCurrentYear();
		updateYears();
		updateSelectedYear();
	}
	
	public int getYearEnd() {
		return mYearEnd;
	}
	
	public void setYearEnd(int end) {
		mYearEnd = end;
		updateYears();
	}
	
	public int getSelectedYear() {
		return mSelectedYear;
	}
	
	public void setSelectedYear(int year) {
		mSelectedYear = year;
		updateSelectedYear();
	}
	
	public int getCurrentYear() {
		return Integer.valueOf(String.valueOf(getDataValue(getCurrentItemPosition())));
	}
}