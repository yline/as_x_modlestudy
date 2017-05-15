package com.keyclear;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		KeyClearEditText editText = (KeyClearEditText) findViewById(R.id.et_key);
		editText.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});
	}
}
