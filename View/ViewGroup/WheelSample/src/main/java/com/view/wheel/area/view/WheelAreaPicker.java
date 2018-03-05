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
    private List<City> mCityList;
    private List<String> mProvinceName, mCityName;

    private AssetManager mAssetManager;
    private LayoutParams mLayoutParams;
    private WheelPicker mWPProvince, mWPCity, mWPArea;

    public WheelAreaPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayoutParams();
        initView(context);

        mProvinceList = getJsonDataFromAssets(mAssetManager);
        obtainProvinceData();
        addListenerToWheelPicker();
    }

    @SuppressWarnings("unchecked")
    private List<Province> getJsonDataFromAssets(AssetManager assetManager) {
        List<Province> provinceList = new ArrayList<>();

        List<City> beijingList = new ArrayList<>();
        beijingList.add(new City("北京", Arrays.asList(new String[]{"朝阳", "南昌"})));
        provinceList.add(new Province("北京", beijingList));

        List<City> zhejiangList = new ArrayList<>();
        zhejiangList.add(new City("杭州", Arrays.asList(new String[]{"滨江", "江干"})));
        zhejiangList.add(new City("宁波", Arrays.asList(new String[]{"沿海", "市中心"})));
        provinceList.add(new Province("浙江", zhejiangList));

        return provinceList;
    }

    private void initLayoutParams() {
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(5, 5, 5, 5);
        mLayoutParams.width = 0;
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);

        mAssetManager = context.getAssets();

        mProvinceName = new ArrayList<>();
        mCityName = new ArrayList<>();

        mWPProvince = new WheelPicker(context);
        mWPCity = new WheelPicker(context);
        mWPArea = new WheelPicker(context);

        initWheelPicker(mWPProvince, 1);
        initWheelPicker(mWPCity, 1.5f);
        initWheelPicker(mWPArea, 1.5f);
    }

    private void initWheelPicker(WheelPicker wheelPicker, float weight) {
        mLayoutParams.weight = weight;
        wheelPicker.setItemTextSize(dip2px(getContext(), ITEM_TEXT_SIZE));
        wheelPicker.setSelectedItemTextColor(Color.parseColor(SELECTED_ITEM_COLOR));
        wheelPicker.setCurved(true);
        wheelPicker.setLayoutParams(mLayoutParams);
        addView(wheelPicker);
    }

    private void obtainProvinceData() {
        for (Province province : mProvinceList) {
            mProvinceName.add(province.getName());
        }
        mWPProvince.setData(mProvinceName);
        setCityAndAreaData(PROVINCE_INITIAL_INDEX);
    }

    private void addListenerToWheelPicker() {
        //监听省份的滑轮,根据省份的滑轮滑动的数据来设置市跟地区的滑轮数据
        mWPProvince.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                //获得该省所有城市的集合
                mCityList = mProvinceList.get(position).getCity();
                setCityAndAreaData(position);
            }
        });

        mWPCity.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                //获取城市对应的城区的名字
                mWPArea.setData(mCityList.get(position).getArea());
            }
        });
    }

    private void setCityAndAreaData(int position) {
        //获得该省所有城市的集合
        mCityList = mProvinceList.get(position).getCity();
        //获取所有city的名字
        //重置先前的城市集合数据
        mCityName.clear();
        for (City city : mCityList) {
            mCityName.add(city.getName());
        }
        mWPCity.setData(mCityName);
        mWPCity.setSelectedItemPosition(0);
        //获取第一个城市对应的城区的名字
        //重置先前的城区集合的数据
        mWPArea.setData(mCityList.get(0).getArea());
        mWPArea.setSelectedItemPosition(0);
    }

    public String getProvince() {
        return mProvinceList.get(mWPProvince.getCurrentItemPosition()).getName();
    }

    public String getCity() {
        return mCityList.get(mWPCity.getCurrentItemPosition()).getName();
    }

    public String getArea() {
        return mCityList.get(mWPCity.getCurrentItemPosition()).getArea().get(mWPArea.getCurrentItemPosition());
    }

    public void hideArea() {
        this.removeViewAt(2);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
