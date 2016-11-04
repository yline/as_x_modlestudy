package com.view.spinner.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.view.spinner.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;
import com.yline.log.LogFileUtil;

import java.util.Arrays;

public class MainActivity extends BaseAppCompatActivity
{
	private String[] commentString = {
			"String demo1", "String demo2", "String demo3", "String demo4", "String demo5",
			"String demo6", "String demo7", "String demo8", "String demo9", "String demo10",
			"String demo11", "String demo12", "String demo13", "String demo14"};

	private Spinner spinner;

	private SpinAdapter spinAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		spinner = (Spinner) findViewById(R.id.spinner);
		spinAdapter = new SpinAdapter(MainActivity.this);
		spinner.setAdapter(spinAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				LogFileUtil.v("OnItemSelected position = " + position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
		
		spinAdapter.addAll(Arrays.asList(commentString));
	}

	private class SpinAdapter extends CommonListAdapter<String>
	{

		public SpinAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_spinner;
		}

		@Override
		protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			viewHolder.setText(R.id.tv_item, sList.get(i));
		}
	}
}
