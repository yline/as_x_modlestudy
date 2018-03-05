package com.wheel.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.util.Arrays;
import java.util.List;

/**
 * 帮助类，为了解脱单个类负载，太多内容
 *
 * @author yline 2018/3/5 -- 13:07
 * @version 1.0.0
 */
public class WheelPickerAttr {
    private static final String[] DEFAULT_DATA = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    /**
     * 数据源
     */
    private List data;

    /**
     * 数据项文本尺寸
     */
    private int itemTextSize;

    /**
     * 最宽的文本
     */
    private String maxWidthText;

    /**
     * 滚轮选择器中可见的数据项数量
     */
    private int visibleItemCount;

    /**
     * 当前被选中的数据项所显示的数据在数据源中的位置
     */
    private int selectedItemPosition;

    /**
     * 滚轮选择器的每一个数据项文本是否拥有相同的宽度
     */
    private boolean hasSameWidth;

    /**
     * 滚轮选择器中最宽或最高的文本在数据源中的位置
     */
    private int textMaxWidthPosition;

    /**
     * 数据项文本颜色以及被选中的数据项文本颜色
     */
    private int itemTextColor, selectedItemTextColor;

    /**
     * 数据项之间间距
     */
    private int itemSpace;

    /**
     * 数据是否循环展示
     */
    private boolean isCyclic;

    /**
     * 是否显示指示器
     */
    private boolean hasIndicator;

    /**
     * 指示器颜色
     */
    private int indicatorColor;

    /**
     * 指示器尺寸
     */
    private int indicatorSize;

    /**
     * 是否显示幕布
     */
    private boolean hasCurtain;

    /**
     * 幕布颜色
     */
    private int curtainColor;

    /**
     * 是否显示空气感效果
     */
    private boolean hasAtmospheric;

    /**
     * 滚轮是否为卷曲效果
     */
    private boolean isCurved;

    /**
     * 数据项对齐方式
     */
    private int itemAlign;

    public WheelPickerAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker);
        int idData = a.getResourceId(R.styleable.WheelPicker_wheel_data, 0);

        data = (idData == 0) ? Arrays.asList(DEFAULT_DATA) : Arrays.asList(context.getResources().getStringArray(idData));
        itemTextSize = a.getDimensionPixelSize(R.styleable.WheelPicker_wheel_item_text_size, sp2px(context, 18));
        maxWidthText = a.getString(R.styleable.WheelPicker_wheel_maximum_width_text);
        visibleItemCount = a.getInt(R.styleable.WheelPicker_wheel_visible_item_count, 7);
        selectedItemPosition = a.getInt(R.styleable.WheelPicker_wheel_selected_item_position, 0);
        hasSameWidth = a.getBoolean(R.styleable.WheelPicker_wheel_same_width, false);
        textMaxWidthPosition = a.getInt(R.styleable.WheelPicker_wheel_maximum_width_text_position, -1);
        itemTextColor = a.getColor(R.styleable.WheelPicker_wheel_item_text_color, 0xFF888888);
        selectedItemTextColor = a.getColor(R.styleable.WheelPicker_wheel_selected_item_text_color, -1);
        itemSpace = a.getDimensionPixelSize(R.styleable.WheelPicker_wheel_item_space, dp2px(context, 10));
        isCyclic = a.getBoolean(R.styleable.WheelPicker_wheel_cyclic, false);
        hasIndicator = a.getBoolean(R.styleable.WheelPicker_wheel_indicator, false);
        indicatorColor = a.getColor(R.styleable.WheelPicker_wheel_indicator_color, 0xFFEE3333);
        indicatorSize = a.getDimensionPixelSize(R.styleable.WheelPicker_wheel_indicator_size, dp2px(context, 1));
        hasCurtain = a.getBoolean(R.styleable.WheelPicker_wheel_curtain, false);
        curtainColor = a.getColor(R.styleable.WheelPicker_wheel_curtain_color, 0x88FFFFFF);
        hasAtmospheric = a.getBoolean(R.styleable.WheelPicker_wheel_atmospheric, false);
        isCurved = a.getBoolean(R.styleable.WheelPicker_wheel_curved, false);
        itemAlign = a.getInt(R.styleable.WheelPicker_wheel_item_align, WheelPicker.ALIGN_CENTER);
        a.recycle();
    }

    public List getData() {
        return data;
    }

    public Object getData(int index) {
        return data.get(index);
    }

    public void setData(List mData) {
        this.data = mData;
    }

    public int getItemTextSize() {
        return itemTextSize;
    }

    public void setItemTextSize(int mItemTextSize) {
        this.itemTextSize = mItemTextSize;
    }

    public String getMaxWidthText() {
        return maxWidthText;
    }

    public void setMaxWidthText(String mMaxWidthText) {
        this.maxWidthText = mMaxWidthText;
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    public void setVisibleItemCount(int visibleItemCount) {
        this.visibleItemCount = visibleItemCount;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    public boolean isHasSameWidth() {
        return hasSameWidth;
    }

    public void setHasSameWidth(boolean hasSameWidth) {
        this.hasSameWidth = hasSameWidth;
    }

    public int getTextMaxWidthPosition() {
        return textMaxWidthPosition;
    }

    public void setTextMaxWidthPosition(int textMaxWidthPosition) {
        this.textMaxWidthPosition = textMaxWidthPosition;
    }

    public int getItemTextColor() {
        return itemTextColor;
    }

    public void setItemTextColor(int itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

    public int getSelectedItemTextColor() {
        return selectedItemTextColor;
    }

    public void setSelectedItemTextColor(int selectedItemTextColor) {
        this.selectedItemTextColor = selectedItemTextColor;
    }

    public int getItemSpace() {
        return itemSpace;
    }

    public void setItemSpace(int itemSpace) {
        this.itemSpace = itemSpace;
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }

    public boolean isHasIndicator() {
        return hasIndicator;
    }

    public void setHasIndicator(boolean hasIndicator) {
        this.hasIndicator = hasIndicator;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public int getIndicatorSize() {
        return indicatorSize;
    }

    public void setIndicatorSize(int indicatorSize) {
        this.indicatorSize = indicatorSize;
    }

    public boolean isHasCurtain() {
        return hasCurtain;
    }

    public void setHasCurtain(boolean hasCurtain) {
        this.hasCurtain = hasCurtain;
    }

    public int getCurtainColor() {
        return curtainColor;
    }

    public void setCurtainColor(int curtainColor) {
        this.curtainColor = curtainColor;
    }

    public boolean isHasAtmospheric() {
        return hasAtmospheric;
    }

    public void setHasAtmospheric(boolean hasAtmospheric) {
        this.hasAtmospheric = hasAtmospheric;
    }

    public boolean isCurved() {
        return isCurved;
    }

    public void setCurved(boolean curved) {
        isCurved = curved;
    }

    public int getItemAlign() {
        return itemAlign;
    }

    public void setItemAlign(int itemAlign) {
        this.itemAlign = itemAlign;
    }

    /**
     * dp to px
     *
     * @param context 上下文
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp to px
     *
     * @param context 上下文
     * @param spValue sp
     * @return px
     */
    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue,
                context.getResources().getDisplayMetrics());
    }
}
