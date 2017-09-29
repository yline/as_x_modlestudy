package com.view.widget.label.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.view.widget.label.WidgetFlowAble;
import com.view.widget.label.sample.R;
import com.view.widget.label.widget.FlowLayout;
import com.view.widget.label.widget.LabelAdapter;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;

public class WidgetFlowAble3Activity extends BaseAppCompatActivity
{
	private WidgetFlowAble clickFlowAble, selectFlowAble, pressFlowAble;

	private boolean isOldSelected;

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, WidgetFlowAble3Activity.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_flow_able_three);

		clickFlowAble = new WidgetFlowAble(this, R.id.flow_label_drawable_one)
		{
			@Override
			protected int getItemResourceId()
			{
				return super.getItemResourceId();
			}
		};
		clickFlowAble.setMaxSelectCount(3);
		clickFlowAble.setMinSelectCount(1);
		clickFlowAble.setOnLabelClickListener(new LabelAdapter.OnLabelClickListener()
		{
			@Override
			public boolean onLabelClick(FlowLayout container, View view, Object o, int position)
			{
				clickFlowAble.toggleSpecialState(position, clickFlowAble.getDataSize() - 1);
				return false;
			}
		});

		selectFlowAble = new WidgetFlowAble(this, R.id.flow_label_drawable_two)
		{
			@Override
			protected int getItemResourceId()
			{
				return R.layout.activity_label_item_flow_able_select;
			}
		};
		selectFlowAble.setMaxSelectCount(3);
		selectFlowAble.setMinSelectCount(1);

		pressFlowAble = new WidgetFlowAble(this, R.id.flow_label_drawable_three)
		{
			@Override
			protected int getItemResourceId()
			{
				return R.layout.activity_label_item_flow_able_press;
			}
		};
		pressFlowAble.setMaxSelectCount(3);
		pressFlowAble.setMinSelectCount(1);

		clickFlowAble.setDataList(StrConstant.getListOne(10));
		selectFlowAble.setDataList(StrConstant.getListTwo(10));
		pressFlowAble.setDataList(StrConstant.getListThree(10));
	}
}
