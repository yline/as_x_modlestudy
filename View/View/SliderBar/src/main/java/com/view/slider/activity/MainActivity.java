package com.view.slider.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.view.slider.R;
import com.view.slider.SideBarFragment;

public class MainActivity extends AppCompatActivity
{
	private FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentTransaction.add(R.id.ll_show, new SideBarFragment()).commit();
	}
}
