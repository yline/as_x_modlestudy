package com.json.activity;

import android.os.Bundle;
import android.view.View;

import com.data.network.json.R;
import com.json.gson.parse.WifiPolicyParse;
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
				LogFileUtil.v(MainApplication.TAG, "onClick btn_gson");
				new WifiPolicyParse().test(WifiPolicyParse.JsonStr);
			}
		});
	}

}
