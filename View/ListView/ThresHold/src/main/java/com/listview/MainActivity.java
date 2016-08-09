package com.listview;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.listview.common.CommonListAdapterInstance;
import com.listview.threshold.instance.ArrayAdapterInstance;
import com.listview.threshold.instance.BaseAdapterInstance;
import com.listview.threshold.instance.SimpleAdapterInstance;
import com.view.listview.R;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
	private ListView mLv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLv = (ListView) findViewById(R.id.lv_show);

		findViewById(R.id.btn_simple_adapter_instance).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_simple_adapter_instance");

				SimpleAdapterInstance sInstance = new SimpleAdapterInstance();
				sInstance.setData();
				sInstance.show(MainActivity.this, mLv);
			}
		});

		findViewById(R.id.btn_array_adapter_instance).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_array_adapter_instance");

				ArrayAdapterInstance aInstance = new ArrayAdapterInstance();
				aInstance.setData();
				aInstance.show(MainActivity.this, mLv);
			}
		});

		findViewById(R.id.btn_base_adapter_instance).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_base_adapter_instance");

				BaseAdapterInstance bInstance = new BaseAdapterInstance();
				bInstance.setData();
				bInstance.show(MainActivity.this, mLv);
			}
		});

		findViewById(R.id.btn_common_listadapter_instance).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "btn_common_listadapter_instance");

				new CommonListAdapterInstance().show(MainActivity.this, mLv);
			}
		});
	}

}
