package com.view.widget.label.sample.activity;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestFragment;

public class WidgetLabelFragment extends BaseTestFragment
{
	public static WidgetLabelFragment newInstance()
	{
		return new WidgetLabelFragment();
	}

	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("WidgetFlow", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				WidgetFlowActivity.actionStart(getContext());
			}
		});

		addButton("WidgetFlowAble", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				WidgetFlowAbleActivity.actionStart(getContext());
			}
		});

		addButton("WidgetFlowAble Single", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				WidgetFlowSingleActivity.actionStart(getContext());
			}
		});

		addButton("WidgetFlowAble Click+Select+Press", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				WidgetFlowAble3Activity.actionStart(getContext());
			}
		});
	}
}
