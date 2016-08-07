package com.dm.state.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.design.pattern.state.R;
import com.dm.state.power.TvController;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_state).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TvController tvController = new TvController();
				tvController.powerOn();
				tvController.nextChannel();
				tvController.turnUp();
				tvController.powerOff();
				tvController.turnUp();
			}
		});

		findViewById(R.id.btn_state_indirect).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TvController tvController = new TvController();
				tvController.powerOn();
				tvController.getTvStateInstance().nextChannel();
				tvController.getTvStateInstance().turnUp();
				tvController.powerOff();
				tvController.getTvStateInstance().turnUp();
			}
		});
	}

}
