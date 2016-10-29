package com.theory.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.theory.sample.R;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn_one = (Button) findViewById(R.id.btn_one);

		btn_one.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent myIntent = new Intent();
				myIntent.setClass(MainActivity.this, MineMineActivity.class);
				startActivity(myIntent);
			}
		});
	}
}
