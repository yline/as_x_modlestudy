package com.gridview.threshold.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gridview.threshold.GridViewBean;
import com.gridview.threshold.R;
import com.yline.base.BaseFragmentActivity;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity
{
	private GridViewFragment gridViewFragment;

	private EditText etNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etNumber = (EditText) findViewById(R.id.et_number);

		gridViewFragment = new GridViewFragment();

		getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment, gridViewFragment).commit();

		findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String content = etNumber.getText().toString().trim();
				int number = 0;
				if (!TextUtils.isEmpty(content))
				{
					number = Integer.parseInt(content);
				}
				LogFileUtil.v("MainActivity", "btn_update number = " + number);

				List<GridViewBean> list = new ArrayList<GridViewBean>();
				for (int i = 0; i < number; i++)
				{
					GridViewBean bean = new GridViewBean(R.mipmap.ic_launcher, "name - " + i);
					list.add(bean);
				}

				gridViewFragment.updateBeans(list);
			}
		});
	}
}
