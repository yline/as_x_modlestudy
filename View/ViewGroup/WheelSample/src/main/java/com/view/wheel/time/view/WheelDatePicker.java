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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WheelDatePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener, IDebug {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());

    private WheelYearPicker mPickerYear;
    private WheelMonthPicker mPickerMonth;
    private WheelDayPicker mPickerDay;

    private OnDateSelectedListener mListener;

    private TextView mTVYear, mTVMonth, mTVDay;
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
        mPickerYear.setOnItemSelectedListener(this);
        mPickerMonth.setOnItemSelectedListener(this);
        mPickerDay.setOnItemSelectedListener(this);

        setMaximumWidthTextYear();
        mPickerMonth.setMaximumWidthText("00");
        mPickerDay.setMaximumWidthText("00");

        mTVYear = (TextView) findViewById(R.id.wheel_date_picker_year_tv);
        mTVMonth = (TextView) findViewById(R.id.wheel_date_picker_month_tv);
        mTVDay = (TextView) findViewById(R.id.wheel_date_picker_day_tv);

        mYear = mPickerYear.getCurrentYear();
        mMonth = mPickerMonth.getCurrentMonth();
        mDay = mPickerDay.getCurrentDay();
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

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        if (picker.getId() == R.id.wheel_date_picker_year) {
            mYear = (int) data;
            mPickerDay.setYear(mYear);
        } else if (picker.getId() == R.id.wheel_date_picker_month) {
            mMonth = (int) data;
            mPickerDay.setMonth(mMonth);
        }
        mDay = mPickerDay.getCurrentDay();
        String date = mYear + "-" + mMonth + "-" + mDay;
        if (null != mListener) {
            try {
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

    public int getVisibleItemCount() {
        if (mPickerYear.getVisibleItemCount() == mPickerMonth.getVisibleItemCount()
                && mPickerMonth.getVisibleItemCount() == mPickerDay.getVisibleItemCount()) {
            return mPickerYear.getVisibleItemCount();
        }
        throw new ArithmeticException("Can not get visible item count correctly from" +
                "WheelDatePicker!");
    }

    public void setVisibleItemCount(int count) {
        mPickerYear.setVisibleItemCount(count);
        mPickerMonth.setVisibleItemCount(count);
        mPickerDay.setVisibleItemCount(count);
    }

    public boolean isCyclic() {
        return mPickerYear.isCyclic() && mPickerMonth.isCyclic() && mPickerDay.isCyclic();
    }

    public void setCyclic(boolean isCyclic) {
        mPickerYear.setCyclic(isCyclic);
        mPickerMonth.setCyclic(isCyclic);
        mPickerDay.setCyclic(isCyclic);
    }

    public int getSelectedItemTextColor() {
        if (mPickerYear.getSelectedItemTextColor() == mPickerMonth.getSelectedItemTextColor()
                && mPickerMonth.getSelectedItemTextColor() == mPickerDay.getSelectedItemTextColor()) {
            return mPickerYear.getSelectedItemTextColor();
        }
        throw new RuntimeException("Can not get color of selected item text correctly from" +
                "WheelDatePicker!");
    }

    public void setSelectedItemTextColor(int color) {
        mPickerYear.setSelectedItemTextColor(color);
        mPickerMonth.setSelectedItemTextColor(color);
        mPickerDay.setSelectedItemTextColor(color);
    }

    public int getItemTextColor() {
        if (mPickerYear.getItemTextColor() == mPickerMonth.getItemTextColor()
                && mPickerMonth.getItemTextColor() == mPickerDay.getItemTextColor()) {
            return mPickerYear.getItemTextColor();
        }
        throw new RuntimeException("Can not get color of item text correctly from" +
                "WheelDatePicker!");
    }

    public void setItemTextColor(int color) {
        mPickerYear.setItemTextColor(color);
        mPickerMonth.setItemTextColor(color);
        mPickerDay.setItemTextColor(color);
    }

    public int getItemTextSize() {
        if (mPickerYear.getItemTextSize() == mPickerMonth.getItemTextSize()
                && mPickerMonth.getItemTextSize() == mPickerDay.getItemTextSize()) {
            return mPickerYear.getItemTextSize();
        }
        throw new RuntimeException("Can not get size of item text correctly from" +
                "WheelDatePicker!");
    }

    public void setItemTextSize(int size) {
        mPickerYear.setItemTextSize(size);
        mPickerMonth.setItemTextSize(size);
        mPickerDay.setItemTextSize(size);
    }

    public int getItemSpace() {
        if (mPickerYear.getItemSpace() == mPickerMonth.getItemSpace()
                && mPickerMonth.getItemSpace() == mPickerDay.getItemSpace()) {
            return mPickerYear.getItemSpace();
        }
        throw new RuntimeException("Can not get item space correctly from WheelDatePicker!");
    }

    public void setItemSpace(int space) {
        mPickerYear.setItemSpace(space);
        mPickerMonth.setItemSpace(space);
        mPickerDay.setItemSpace(space);
    }

    public void setIndicator(boolean hasIndicator) {
        mPickerYear.setIndicator(hasIndicator);
        mPickerMonth.setIndicator(hasIndicator);
        mPickerDay.setIndicator(hasIndicator);
    }

    public boolean hasIndicator() {
        return mPickerYear.hasIndicator() && mPickerMonth.hasIndicator() && mPickerDay.hasIndicator();
    }

    public int getIndicatorSize() {
        if (mPickerYear.getIndicatorSize() == mPickerMonth.getIndicatorSize()
                && mPickerMonth.getIndicatorSize() == mPickerDay.getIndicatorSize()) {
            return mPickerYear.getIndicatorSize();
        }
        throw new RuntimeException("Can not get indicator size correctly from WheelDatePicker!");
    }

    public void setIndicatorSize(int size) {
        mPickerYear.setIndicatorSize(size);
        mPickerMonth.setIndicatorSize(size);
        mPickerDay.setIndicatorSize(size);
    }

    public int getIndicatorColor() {
        if (mPickerYear.getCurtainColor() == mPickerMonth.getCurtainColor()
                && mPickerMonth.getCurtainColor() == mPickerDay.getCurtainColor()) {
            return mPickerYear.getCurtainColor();
        }
        throw new RuntimeException("Can not get indicator color correctly from WheelDatePicker!");
    }

    public void setIndicatorColor(int color) {
        mPickerYear.setIndicatorColor(color);
        mPickerMonth.setIndicatorColor(color);
        mPickerDay.setIndicatorColor(color);
    }

    public void setCurtain(boolean hasCurtain) {
        mPickerYear.setCurtain(hasCurtain);
        mPickerMonth.setCurtain(hasCurtain);
        mPickerDay.setCurtain(hasCurtain);
    }

    public boolean hasCurtain() {
        return mPickerYear.hasCurtain() && mPickerMonth.hasCurtain() && mPickerDay.hasCurtain();
    }

    public int getCurtainColor() {
        if (mPickerYear.getCurtainColor() == mPickerMonth.getCurtainColor()
                && mPickerMonth.getCurtainColor() == mPickerDay.getCurtainColor()) {
            return mPickerYear.getCurtainColor();
        }
        throw new RuntimeException("Can not get curtain color correctly from WheelDatePicker!");
    }

    public void setCurtainColor(int color) {
        mPickerYear.setCurtainColor(color);
        mPickerMonth.setCurtainColor(color);
        mPickerDay.setCurtainColor(color);
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        mPickerYear.setAtmospheric(hasAtmospheric);
        mPickerMonth.setAtmospheric(hasAtmospheric);
        mPickerDay.setAtmospheric(hasAtmospheric);
    }

    public boolean hasAtmospheric() {
        return mPickerYear.hasAtmospheric() && mPickerMonth.hasAtmospheric() &&
                mPickerDay.hasAtmospheric();
    }

    public boolean isCurved() {
        return mPickerYear.isCurved() && mPickerMonth.isCurved() && mPickerDay.isCurved();
    }

    public void setCurved(boolean isCurved) {
        mPickerYear.setCurved(isCurved);
        mPickerMonth.setCurved(isCurved);
        mPickerDay.setCurved(isCurved);
    }

    public Typeface getTypeface() {
        if (mPickerYear.getTypeface().equals(mPickerMonth.getTypeface())
                && mPickerMonth.getTypeface().equals(mPickerDay.getTypeface())) {
            return mPickerYear.getTypeface();
        }
        throw new RuntimeException("Can not get typeface correctly from WheelDatePicker!");
    }

    public void setTypeface(Typeface tf) {
        mPickerYear.setTypeface(tf);
        mPickerMonth.setTypeface(tf);
        mPickerDay.setTypeface(tf);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mListener = listener;
    }

    public Date getCurrentDate() {
        String date = mYear + "-" + mMonth + "-" + mDay;
        try {
            return SDF.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getItemAlignYear() {
        return mPickerYear.getItemAlign();
    }

    public void setItemAlignYear(int align) {
        mPickerYear.setItemAlign(align);
    }

    public int getItemAlignMonth() {
        return mPickerMonth.getItemAlign();
    }

    public void setItemAlignMonth(int align) {
        mPickerMonth.setItemAlign(align);
    }

    public int getItemAlignDay() {
        return mPickerDay.getItemAlign();
    }

    public void setItemAlignDay(int align) {
        mPickerDay.setItemAlign(align);
    }

    public WheelYearPicker getWheelYearPicker() {
        return mPickerYear;
    }

    public WheelMonthPicker getWheelMonthPicker() {
        return mPickerMonth;
    }

    public WheelDayPicker getWheelDayPicker() {
        return mPickerDay;
    }

    public TextView getTextViewYear() {
        return mTVYear;
    }

    public TextView getTextViewMonth() {
        return mTVMonth;
    }

    public TextView getTextViewDay() {
        return mTVDay;
    }

    public void setYearFrame(int start, int end) {
        mPickerYear.setYearFrame(start, end);
    }

    public int getYearStart() {
        return mPickerYear.getYearStart();
    }

    public void setYearStart(int start) {
        mPickerYear.setYearStart(start);
    }

    public int getYearEnd() {
        return mPickerYear.getYearEnd();
    }

    public void setYearEnd(int end) {
        mPickerYear.setYearEnd(end);
    }

    public int getSelectedYear() {
        return mPickerYear.getSelectedYear();
    }

    public void setSelectedYear(int year) {
        mYear = year;
        mPickerYear.setSelectedYear(year);
        mPickerDay.setYear(year);
    }

    public int getCurrentYear() {
        return mPickerYear.getCurrentYear();
    }

    public int getSelectedMonth() {
        return mPickerMonth.getSelectedMonth();
    }

    public void setSelectedMonth(int month) {
        mMonth = month;
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setMonth(month);
    }

    public int getCurrentMonth() {
        return mPickerMonth.getCurrentMonth();
    }

    public int getSelectedDay() {
        return mPickerDay.getSelectedDay();
    }

    public void setSelectedDay(int day) {
        mDay = day;
        mPickerDay.setSelectedDay(day);
    }

    public int getCurrentDay() {
        return mPickerDay.getCurrentDay();
    }

    public void setYearAndMonth(int year, int month) {
        mYear = year;
        mMonth = month;
        mPickerYear.setSelectedYear(year);
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setYearAndMonth(year, month);
    }

    public int getYear() {
        return getSelectedYear();
    }

    public void setYear(int year) {
        mYear = year;
        mPickerYear.setSelectedYear(year);
        mPickerDay.setYear(year);
    }

    public int getMonth() {
        return getSelectedMonth();
    }

    public void setMonth(int month) {
        mMonth = month;
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setMonth(month);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(WheelDatePicker picker, Date date);
    }
}