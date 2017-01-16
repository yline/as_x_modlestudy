package com.sqlite.activity;

import android.os.Bundle;
import android.view.View;

import com.sqlite.Helper.MyDbManager;
import com.sqlite.R;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initMyDbView();
	}

	private void initMyDbView()
	{
		findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MyDbManager.getInstance().insert(1, "f21", 18);
				MyDbManager.getInstance().insert(2, "fatenliyer", 19);
				MyDbManager.getInstance().insert(3, "delete", 20);
				MyDbManager.getInstance().insert(4, "yline", 21);
			}
		});

		findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MyDbManager.getInstance().deleteById(3);
			}
		});

		findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MyDbManager.getInstance().updateById(1, "update", 17);
			}
		});
	}
}
