package com.number.picker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.NumberPicker;

import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseAppCompatActivity
{
	private NumberPicker numberPicker;

	private String[] stringList = new String[]{"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天枰座", "天蝎座", "射手座", "摩羯座"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		numberPicker = (NumberPicker) findViewById(R.id.number_picker);
		numberPicker.setDisplayedValues(stringList);
		numberPicker.setMinValue(0);
		numberPicker.setMaxValue(11);
		numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		// numberPicker.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

		numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
		{
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal)
			{
				LogFileUtil.v("newVal = " + newVal + ", newVal = " + stringList[newVal]);
			}
		});

		findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new NumberPickerReflect()
						.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light))
						.commit(numberPicker);
			}
		});
	}
}
