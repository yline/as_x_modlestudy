package com.view.wheel.time.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wheel.lib.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 月份选择器
 * <p/>
 * Picker for Months
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelMonthPicker extends WheelPicker<Integer> {
    private int mSelectedMonth;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            data.add(i);
        }
        super.setData(data);

        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth - 1);
    }

    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    public void setSelectedMonth(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    public int getCurrentMonth() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}