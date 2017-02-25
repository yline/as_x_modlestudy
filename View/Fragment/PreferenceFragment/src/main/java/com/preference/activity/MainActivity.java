package com.preference.activity;

import android.os.Bundle;

import com.preference.fragment.MainFragment;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		getFragmentManager().beginTransaction().add(android.R.id.content, new MainFragment()).commit();
	}
}
