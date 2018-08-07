package com.wheel.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供一种方式，让上层可以重写 valueOf方法
 *
 * @author yline
 * @times 2018/8/7 -- 9:57
 */
public class WheelPickerAdapter<T> {
	/**
	 * 滚动状态标识值
	 */
	public static final int SCROLL_STATE_IDLE = 0, SCROLL_STATE_DRAGGING = 1, SCROLL_STATE_SCROLLING = 2;
	
	private List<T> data = new ArrayList<>();
	
	private WheelPicker.OnItemSelectedListener<T> mOnItemSelectedListener;
	
	private WheelPicker.OnWheelChangeListener<T> mOnWheelChangeListener;
	
	public int getCount() {
		return data.size();
	}
	
	public String getDataValue(int position) {
		return valueOf(data.get(position));
	}
	
	/**
	 * 获取泛型的实际展示数据；可实现重写，从而支持任何的数据类型
	 *
	 * @param t 泛型数据
	 * @return 展示的String
	 */
	public String valueOf(T t) {
		return (null == t ? "null" : t.toString());
	}
	
	/**
	 * 设置数据列表
	 * 数据源可以是任意类型，但是需要注意的是WheelPicker在绘制数据的时候会将数据转换成String类型
	 * 在没有设置数据源的情况下滚轮选择器会设置一个默认的数据源作为展示
	 * 为滚轮选择器设置数据源会重置滚轮选择器的各项状态，具体行为参考
	 *
	 * @param data 数据列表
	 */
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public void notifyDataSetChanged(WheelPicker wheelPicker) {
		wheelPicker.notifyDataSetChanged();
	}
	
	/**
	 * 设置滚轮Item选中监听器
	 *
	 * @param listener 滚轮Item选中监听器{@link WheelPicker.OnItemSelectedListener}
	 */
	public void setOnItemSelectedListener(WheelPicker.OnItemSelectedListener<T> listener) {
		this.mOnItemSelectedListener = listener;
	}
	
	/**
	 * 设置滚轮滚动状态改变监听器
	 *
	 * @param listener 滚轮滚动状态改变监听器
	 * @see WheelPicker.OnWheelChangeListener
	 */
	public void setOnWheelChangeListener(WheelPicker.OnWheelChangeListener<T> listener) {
		this.mOnWheelChangeListener = listener;
	}
	
	void callItemSelected(WheelPicker pickerView, int position) {
		if (null != mOnItemSelectedListener) {
			mOnItemSelectedListener.onItemSelected(pickerView, data.get(position), position);
		}
	}
	
	void callWheelScrolled(int offset) {
		if (null != mOnWheelChangeListener) {
			mOnWheelChangeListener.onWheelScrolled(offset);
		}
	}
	
	void callWheelScrollStateChanged(int state) {
		if (null != mOnWheelChangeListener) {
			mOnWheelChangeListener.onWheelScrollStateChanged(state);
		}
	}
	
	void callWheelSelected(int position) {
		if (null != mOnWheelChangeListener) {
			mOnWheelChangeListener.onWheelSelected(data.get(position), position);
		}
	}
}
