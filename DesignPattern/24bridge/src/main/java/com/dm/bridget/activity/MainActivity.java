package com.dm.bridget.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.bridge.R;
import com.dm.bridget.addBridge.Ordinary;
import com.dm.bridget.addBridge.Sugar;
import com.dm.bridget.implConcrete.Coffee;
import com.dm.bridget.implConcrete.LargeCoffee;
import com.dm.bridget.implConcrete.SmallCoffee;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_bridget).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(MainApplication.TAG, "onClick -> btn_bridget");

				Ordinary ordinary = new Ordinary();
				Sugar sugar = new Sugar();

				Coffee largeOrdinaryCoffee = new LargeCoffee(ordinary);
				largeOrdinaryCoffee.makeCoffee();

				Coffee largeSugarCoffee = new LargeCoffee(sugar);
				largeSugarCoffee.makeCoffee();

				Coffee smallOrdinaryCoffee = new SmallCoffee(ordinary);
				smallOrdinaryCoffee.makeCoffee();

				Coffee smallSugarCoffee = new SmallCoffee(sugar);
				smallSugarCoffee.makeCoffee();
			}
		});
	}

}
