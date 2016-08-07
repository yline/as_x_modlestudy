package com.dm.template;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.template.R;
import com.dm.template.abstracts.AbstractComputer;
import com.dm.template.concrete.CoderComputer;
import com.dm.template.concrete.MilitaryComputer;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_coder).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick btn_coder");

				AbstractComputer coderComputer = new CoderComputer();
				coderComputer.startUp();
			}
		});
		
		findViewById(R.id.btn_military).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick btn_military");

				AbstractComputer militaryComputer = new MilitaryComputer();
				militaryComputer.startUp();
			}
		});
	}

}
