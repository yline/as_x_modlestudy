package com.json.json;

import com.yline.log.LogFileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yline on 2016/10/6.
 */
public class JsonHelper
{
	private static final String TAG = "JsonHelper";

	/**
	 * [{"id":"5","version":"5.5","name":"Angry Birds"},
	 * {"id":"6","version":"7.0","name":"Clash of Clans"},
	 * {"id":"7","version":"3.5","name":"Hey Day"}]
	 * @param jsonStr
	 */
	public void parseJson(String jsonStr)
	{
		try
		{
			JSONArray jsonArray = new JSONArray(jsonStr);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				String version = jsonObject.getString("version");

				LogFileUtil.v(TAG, "id = " + id);
				LogFileUtil.v(TAG, "name = " + name);
				LogFileUtil.v(TAG, "version = " + version);
			}
		}
		catch (JSONException e)
		{
			LogFileUtil.e(TAG, "JSONException", e);
		}
	}

	/**
	 * {
	 * "code":0,
	 * "data":{
	 * "client_id":"by565fa4facdb191",
	 * "data":"ZXlKamW4wPQ==",
	 * "notify_id":"111",
	 * "time":"1449318416",
	 * "type":"T",
	 * "alg":"md5",
	 * "sign":"2c786afe85076653f8510149d3eb500f"
	 * }
	 * }
	 * @param jsonStr
	 */
	public void parseJsonList(String jsonStr)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(jsonStr);
			int code = jsonObject.getInt("code");
			LogFileUtil.v(TAG, "code = " + code);

			// data 的 json数据
			String jsonData = jsonObject.getJSONObject("data").toString();
			LogFileUtil.v(TAG, "jsonData = " + jsonData);
		}
		catch (JSONException e)
		{
			LogFileUtil.e(TAG, "JSONException", e);
		}
	}
}
