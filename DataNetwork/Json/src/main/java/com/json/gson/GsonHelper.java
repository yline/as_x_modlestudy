package com.json.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yline on 2016/10/6.
 */
public class GsonHelper
{
	private static final String TAG = "GsonHelper";

	/**
	 * 生成单行
	 */
	public String genGson()
	{
		Gson gson = new Gson();

		BaseBean bean = new BaseBean(100, "f21");

		String jsonStr = gson.toJson(bean);
		return jsonStr;
	}
	
	/**
	 * 生成多行
	 * @param number
	 * @return
	 */
	public String genGsonList(int number)
	{
		Gson gson = new Gson();
		List<BaseBean> beans = new ArrayList<BaseBean>();
		for (int i = 0; i < number; i++)
		{
			BaseBean bean = new BaseBean(i, "yline - " + i);
			beans.add(bean);
		}

		// 这里将bean转化成jsonStr字符串
		String jsonStr = gson.toJson(beans);
		return jsonStr;
	}

	/**
	 * 解析出内容Gson单行
	 * @param json
	 */
	public void parseGson(String json)
	{
		Gson gson = new Gson();
		BaseBean bean = gson.fromJson(json, BaseBean.class);
		LogFileUtil.v(TAG, bean.toString());
	}

	/**
	 * 解析出GsonList
	 * @param json
	 */
	public void parseGsonList(String json)
	{
		Gson gson = new Gson();
		List<BaseBean> beans = gson.fromJson(json, new TypeToken<List<BaseBean>>()
		{
		}.getType());
		for (BaseBean bean : beans)
		{
			LogFileUtil.v(TAG, bean.toString());
		}
	}
}
