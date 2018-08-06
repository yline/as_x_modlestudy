package com.view.wheel.area.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.view.wheel.area.model.City;
import com.view.wheel.area.model.Province;
import com.wheel.lib.WheelPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据失败，因此会失败
 *
 * @author yline 2018/3/5 -- 15:35
 * @version 1.0.0
 */
public class WheelAreaPicker extends LinearLayout {
	private static final float ITEM_TEXT_SIZE = 18;
	private static final String SELECTED_ITEM_COLOR = "#353535";
	private static final int PROVINCE_INITIAL_INDEX = 0;
	
	private List<Province> mProvinceList;
	
	private LayoutParams mLayoutParams;
	
	private WheelPicker<Province> mProvinceWheelPicker;
	private WheelPicker<City> mCityWheelPicker;
	private WheelPicker<String> mAreaWheelPicker;
	
	public WheelAreaPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView(context);
		initData();
	}
	
	private void initView(Context context) {
		mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLayoutParams.setMargins(5, 5, 5, 5);
		mLayoutParams.width = 0;
		setOrientation(HORIZONTAL);
		
		mProvinceWheelPicker = new WheelPicker<Province>(context) {
			@Override
			public String valueOf(Province province) {
				return province.getName();
			}
		};
		mCityWheelPicker = new WheelPicker<City>(context) {
			@Override
			public String valueOf(City city) {
				return city.getName();
			}
		};
		mAreaWheelPicker = new WheelPicker<>(context);
		
		initWheelPicker(mProvinceWheelPicker, 1);
		initWheelPicker(mCityWheelPicker, 1.5f);
		initWheelPicker(mAreaWheelPicker, 1.5f);
		
		initViewClick();
	}
	
	private void initWheelPicker(WheelPicker wheelPicker, float weight) {
		mLayoutParams.weight = weight;
		wheelPicker.setItemTextSize(dip2px(getContext(), ITEM_TEXT_SIZE));
		wheelPicker.setSelectedItemTextColor(Color.parseColor(SELECTED_ITEM_COLOR));
		wheelPicker.setCurved(true);
		wheelPicker.setLayoutParams(mLayoutParams);
		addView(wheelPicker);
	}
	
	private void initViewClick() {
		// 监听省份的滑轮,根据省份的滑轮滑动的数据来设置市跟地区的滑轮数据
		mProvinceWheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener<Province>() {
			@Override
			public void onItemSelected(WheelPicker picker, Province province, int position) {
				// 获得该省所有城市的集合
				setCityAndAreaData(position);
			}
		});
		
		mCityWheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener<City>() {
			@Override
			public void onItemSelected(WheelPicker picker, City city, int position) {
				// 获取城市对应的城区的名字
				mAreaWheelPicker.setData(city.getArea());
			}
		});
	}
	
	private void initData() {
		List<Province> provinceList = new ArrayList<>();
		
		List<City> beijingList = new ArrayList<>();
		beijingList.add(new City("北京", Arrays.asList("朝阳", "南昌")));
		provinceList.add(new Province("北京", beijingList));
		
		List<City> zhejiangList = new ArrayList<>();
		zhejiangList.add(new City("杭州", Arrays.asList("滨江", "江干")));
		zhejiangList.add(new City("宁波", Arrays.asList("沿海", "市中心")));
		provinceList.add(new Province("浙江", zhejiangList));
		
		mProvinceList = provinceList;
		
		mProvinceWheelPicker.setData(mProvinceList);
		
		obtainProvinceData();
	}
	
	private void obtainProvinceData() {
		setCityAndAreaData(PROVINCE_INITIAL_INDEX);
	}
	
	private void setCityAndAreaData(int position) {
		//		//获得该省所有城市的集合
		//		mCityList = mProvinceList.get(position).getCity();
		//		//获取所有city的名字
		//		//重置先前的城市集合数据
		//		mCityName.clear();
		//		for (City city : mCityList) {
		//			mCityName.add(city.getName());
		//		}
		//		mWPCity.setData(mCityName);
		//		mWPCity.setSelectedItemPosition(0);
		//		//获取第一个城市对应的城区的名字
		//		//重置先前的城区集合的数据
		//		mWPArea.setData(mCityList.get(0).getArea());
		//		mWPArea.setSelectedItemPosition(0);
	}
	
	private int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
