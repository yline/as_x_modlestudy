package com.dm.decorator.activity;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.decorate.R;
import com.dm.decorator.component.Boy;
import com.dm.decorator.component.Person;
import com.dm.decorator.decorator.CheapCloth;
import com.dm.decorator.decorator.ExpensiveCloth;
import com.dm.decorator.decorator.PersonCloth;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Person person = new Boy(); // 实际对象

		findViewById(R.id.btn_cheap_dress).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				PersonCloth cheapCloth = new CheapCloth(person);
				cheapCloth.dressed();
			}
		});

		findViewById(R.id.btn_expensive_dress).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				PersonCloth expensiveCloth = new ExpensiveCloth(person);
				expensiveCloth.dressed();
			}
		});
	}

}
