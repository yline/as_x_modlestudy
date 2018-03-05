package com.view.wheel.time.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wheel.lib.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期选择器
 * <p/>
 * Picker for Day
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelDayPicker extends WheelPicker {
    private static final Map<Integer, List<Integer>> DAYS = new HashMap<>();

    private Calendar mCalendar;

    private int mYear, mMonth;

    private int mSelectedDay;

    public WheelDayPicker(Context context) {
        this(context, null);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCalendar = Calendar.getInstance();

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);

        updateDays();

        mSelectedDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        updateSelectedDay();
    }

    private void updateDays() {
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);

        int days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<Integer> data = DAYS.get(days);
        if (null == data) {
            data = new ArrayList<>();
            for (int i = 1; i <= days; i++) {
                data.add(i);
            }
            DAYS.put(days, data);
        }
        super.setData(data);
    }

    private void updateSelectedDay() {
        setSelectedItemPosition(mSelectedDay - 1);
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public void setSelectedDay(int day) {
        mSelectedDay = day;
        updateSelectedDay();
    }

    public int getCurrentDay() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }

    public void setYearAndMonth(int year, int month) {
        mYear = year;
        mMonth = month - 1;
        updateDays();
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
        updateDays();
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month - 1;
        updateDays();
    }
}