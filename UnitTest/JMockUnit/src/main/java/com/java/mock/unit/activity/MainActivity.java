package com.java.mock.unit.activity;

import android.app.Activity;
import android.os.Bundle;

import com.unit.test.jmockunit.R;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
