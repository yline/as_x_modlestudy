package com.json.activity;

import android.os.Bundle;
import android.view.View;

import com.data.network.json.R;
import com.json.gson.GsonHelper;
import com.json.json.JsonHelper;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_gson).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_gson");
				GsonHelper gsonHelper = new GsonHelper();

				String gsonStr = gsonHelper.genGson();
				LogFileUtil.v(MainApplication.TAG, "gsonStr = " + gsonStr);

				String gsonListStr = gsonHelper.genGsonList(10);
				LogFileUtil.v(MainApplication.TAG, "gsonListStr = " + gsonListStr);

				gsonHelper.parseGson(gsonStr);
				gsonHelper.parseGsonList(gsonListStr);
			}
		});

		findViewById(R.id.btn_json_object).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_json_object");
				JsonHelper jsonHelper = new JsonHelper();

				String jsonStr = "[{\"id\":\"5\",\"version\":\"5.5\",\"name\":\"Angry Birds\"},{\"id\":\"6\",\"version\":\"7.0\",\"name\":\"Clash of Clans\"}, {\"id\":\"7\",\"version\":\"3.5\",\"name\":\"Hey Day\"}]";
				jsonHelper.parseJson(jsonStr);

				String jsonListStr = "{\n" +
						"    \"code\":0,\n" +
						"    \"data\":{\n" +
						"        \"client_id\":\"by565fa4facdb191\",\n" +
						"        \"data\":\"ZXlKamW4wPQ==\",\n" +
						"        \"notify_id\":\"111\",\n" +
						"        \"time\":\"1449318416\",\n" +
						"        \"type\":\"T\",\n" +
						"        \"alg\":\"md5\",\n" +
						"        \"sign\":\"2c786afe85076653f8510149d3eb500f\"\n" +
						"    }\n" +
						"}";
				jsonHelper.parseJsonList(jsonListStr);
			}
		});
	}

}
