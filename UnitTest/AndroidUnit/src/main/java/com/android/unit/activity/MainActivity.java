package com.android.unit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unit.test.androidunit.R;

public class MainActivity extends Activity
{
	private EditText mETParams1;

	private EditText mETParams2;

	private Button mBtnCaculate;

	/** 计算结果 */
	private TextView mTVResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		
		mBtnCaculate.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				int param1 = parseInt(mETParams1);
				int param2 = parseInt(mETParams2);
				mTVResult.setText(param1 + param2 + "");
			}
		});
	}

	private void initView()
	{
		mETParams1 = (EditText) findViewById(R.id.et_param1);
		mETParams2 = (EditText) findViewById(R.id.et_param2);
		mBtnCaculate = (Button) findViewById(R.id.btn_caculate);
		mTVResult = (TextView) findViewById(R.id.tv_result);
	}

	private int parseInt(EditText etParam)
	{
		String paramStr = etParam.getText().toString().trim();
		if (!TextUtils.isEmpty(paramStr))
		{
			return Integer.parseInt(paramStr);
		}
		return 0;
	}
}
