package com.system.handler.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.system.handler.R;
import com.system.handler.TestThread;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity implements TestThread.ThreadCallback
{
	private TextView tvTest;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TestThread testThread = new TestThread();
		testThread.setThreadCallback(this);
		testThread.start();

		tvTest = (TextView) findViewById(R.id.tv_test);
	}

	@Override
	public void onResult(int what, int arg1)
	{
		tvTest.setText(R.string.app_name + " number is " + arg1);
	}
}
