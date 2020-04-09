package com.yline.address.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yline.application.SDKManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责提供数据
 * 1，之所以以回调的方式给，是因为可能用网络请求替代一次性下发数据
 *
 * @author yline 2020-04-08 -- 18:29
 */
public class RegionDataManager {
    public static void getProvinceList(OnDataResultCallback callback) {
        List<RegionBean> regionBeanList = getProvinceData();

        List<String> provinceList = new ArrayList<>();
        for (RegionBean regionBean : regionBeanList) {
            provinceList.add(regionBean.provinceName);
        }

        if (null != callback) {
            callback.onResult(provinceList);
        }
    }

    public static void getCityList(String province, OnDataResultCallback callback) {
        List<RegionBean> regionBeanList = getProvinceData();

        List<String> cityList = new ArrayList<>();
        for (RegionBean regionBean : regionBeanList) {
            if (province.equals(regionBean.provinceName)) {
                for (RegionBean.City city : regionBean.citys) {
                    cityList.add(city.cityName);
                }
            }
        }

        if (null != callback) {
            callback.onResult(cityList);
        }
    }

    public static void getAreaList(String province, String city, OnDataResultCallback callback) {
        List<RegionBean> regionBeanList = getProvinceData();

        List<String> areaList = new ArrayList<>();
        for (RegionBean regionBean : regionBeanList) {
            if (province.equals(regionBean.provinceName)) {
                for (RegionBean.City cityBean : regionBean.citys) {
                    if (city.equals(cityBean.cityName)) {
                        for (RegionBean.City.Area areaBean : cityBean.areas) {
                            areaList.add(areaBean.areaName);
                        }
                    }
                }
            }
        }

        if (null != callback) {
            callback.onResult(areaList);
        }
    }

    /**
     * 第四层数据，数据内容为空。则这就是最后一个
     */
    public static void getStreetList(String province, String city, String area, OnDataResultCallback callback) {
        List<String> streetList = new ArrayList<>();

        if (null != callback) {
            callback.onResult(streetList);
        }
    }

    public interface OnDataResultCallback {
        void onResult(List<String> dataList);
    }

    private static List<RegionBean> mProvinceData;
    private static final String jsonFile = "region.json";// assets文件夹中

    private static List<RegionBean> getProvinceData() {
        if (null == mProvinceData) {
            mProvinceData = getJsonData(SDKManager.getApplication());
        }
        return mProvinceData;
    }

    private static List<RegionBean> getJsonData(Context context) {
        List<RegionBean> result = new ArrayList<>();
        try {
            String jsonString = loadJson2Str(context, jsonFile);
            JsonArray jsonArray = stringToJsonArray(jsonString);
            if (jsonArray == null) {
                return result;
            }

            for (JsonElement j : jsonArray) {
                result.add(new Gson().fromJson(j, RegionBean.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String loadJson2Str(Context context, String fileName) {
        String result = "";
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = context.getAssets().open(fileName);
            bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[4 * 1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            result = new String(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static JsonArray stringToJsonArray(String str) {
        try {
            return new JsonParser().parse(str).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
