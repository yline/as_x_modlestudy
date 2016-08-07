package com.dm.adapter.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.adapter.R;
import com.dm.adapter.composite.CVolt220;
import com.dm.adapter.composite.CVoltAdapter;
import com.dm.adapter.extend.EVoltAdapter;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 类 适配器模式
		findViewById(R.id.btn_extend).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_extend");
				
				EVoltAdapter eVoltAdapter = new EVoltAdapter();
				LogFileUtil.v(MainApplication.TAG, "extend -> 电压 + " + eVoltAdapter.getVolt5());
			}
		});

		// 对象 适配器模式
		findViewById(R.id.btn_composite).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_composite");

				CVoltAdapter cVoltAdapter = new CVoltAdapter(new CVolt220());
				LogFileUtil.v(MainApplication.TAG, "composite -> 电压 + " + cVoltAdapter.getVolt5());
			}
		});
	}

}
