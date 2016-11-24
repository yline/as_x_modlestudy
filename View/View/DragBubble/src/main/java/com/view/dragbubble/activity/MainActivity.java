package com.view.dragbubble.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.view.dragbubble.R;
import com.view.dragbubble.fragment.TestFragment;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	private FragmentManager mFragmentManager = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentManager.beginTransaction().add(R.id.fl_content, new TestFragment()).commit();
	}
}
