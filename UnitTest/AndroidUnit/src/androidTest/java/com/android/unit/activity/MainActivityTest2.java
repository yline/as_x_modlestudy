package com.android.unit.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unit.test.androidunit.R;

public class MainActivityTest2 extends ActivityInstrumentationTestCase2<MainActivity>
{
	private EditText mETParams1;

	private EditText mETParams2;

	private Button mBtnCaculate;

	/** 计算结果 */
	private TextView mTVResult;

	public MainActivityTest2()
	{
		super(MainActivity.class);
	}

	public MainActivityTest2(Class<MainActivity> activityClass)
	{
		super(activityClass);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		/*
		Intent intent = new Intent(getInstrumentation().getTargetContext(), CaculateActivity.class);
        startActivity(intent, null, null);
        */
		checkWidgets();
	}

	private void checkWidgets()
	{
		mETParams1 = (EditText) findViewById(R.id.et_param1);
		assertNotNull(mETParams1);
		assertEquals("", mETParams1.getText().toString());

		mETParams2 = (EditText) findViewById(R.id.et_param2);
		assertNotNull(mETParams2);
		assertEquals("", mETParams2.getText().toString());

		mBtnCaculate = (Button) findViewById(R.id.btn_caculate);
		assertNotNull(mBtnCaculate);

		mTVResult = (TextView) findViewById(R.id.tv_result);
		assertNotNull(mTVResult);
	}

	public void testAdd()
	{
		// 更新UI
		getActivity().runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				mETParams1.setText("2");
				mETParams2.setText("3");
			}
		});

		// 等待UI线程空闲之后再继续执行
		getInstrumentation().waitForIdleSync();

		// 执行点击
		TouchUtils.clickView(MainActivityTest2.this, mBtnCaculate);

		// 判断
		assertEquals("5", mTVResult.getText().toString());
	}

	@SuppressWarnings("unchecked")
	private <T extends View> T findViewById(int id)
	{
		return (T) getActivity().findViewById(id);
	}
}
