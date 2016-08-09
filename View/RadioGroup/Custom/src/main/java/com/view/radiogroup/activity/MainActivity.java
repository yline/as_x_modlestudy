package com.view.radiogroup.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.view.radiogroup.R;
import com.view.radiogroup.RadioGroupFlow;
import com.view.radiogroup.RadioGroupNestLinear;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RadioGroupFlow rg_radiogroupflow_bg = (RadioGroupFlow) findViewById(R.id.rg_radiogroupflow_bg);
		rg_radiogroupflow_bg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if (checkedId == R.id.rb_radiogroupflow_1)
				{
					MainApplication.toast("radiogroupflow_1");
					LogFileUtil.v(MainApplication.TAG, "flow -> onCheckedChanged radiogroupflow_1");
				}
				else if (checkedId == R.id.rb_radiogroupflow_2)
				{
					MainApplication.toast("radiogroupflow_2");
					LogFileUtil.v(MainApplication.TAG, "flow -> onCheckedChanged radiogroupflow_2");
				}
				else if (checkedId == R.id.rb_radiogroupflow_3)
				{
					MainApplication.toast("radiogroupflow_3");
					LogFileUtil.v(MainApplication.TAG, "flow -> onCheckedChanged radiogroupflow_3");
				}
				else if (checkedId == R.id.rb_radiogroupflow_4)
				{
					MainApplication.toast("radiogroupflow_4");
					LogFileUtil.v(MainApplication.TAG, "flow -> onCheckedChanged radiogroupflow_4");
				}
			}
		});

		RadioGroupNestLinear rg_radiogroupnestlinear_bg = (RadioGroupNestLinear) findViewById(R.id.rg_radiogroupnestlinear_bg);
		rg_radiogroupnestlinear_bg.setOnCheckedChangeListener(new RadioGroupNestLinear.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroupNestLinear group, int checkedId)
			{
				if (checkedId == R.id.rb_radiogroupnestlinear_1)
				{
					MainApplication.toast("radiogroupnestlinear_1");
					LogFileUtil.v(MainApplication.TAG, "nestlinear -> onCheckedChanged radiogroupflow_1");
				}
				else if (checkedId == R.id.rb_radiogroupnestlinear_2)
				{
					MainApplication.toast("radiogroupnestlinear_2");
					LogFileUtil.v(MainApplication.TAG, "nestlinear -> onCheckedChanged radiogroupflow_2");
				}
				else if (checkedId == R.id.rb_radiogroupnestlinear_3)
				{
					MainApplication.toast("radiogroupnestlinear_3");
					LogFileUtil.v(MainApplication.TAG, "nestlinear -> onCheckedChanged radiogroupflow_3");
				}
				else if (checkedId == R.id.rb_radiogroupnestlinear_4)
				{
					MainApplication.toast("radiogroupnestlinear_4");
					LogFileUtil.v(MainApplication.TAG, "nestlinear -> onCheckedChanged radiogroupflow_4");
				}
			}
		});
	}

}
