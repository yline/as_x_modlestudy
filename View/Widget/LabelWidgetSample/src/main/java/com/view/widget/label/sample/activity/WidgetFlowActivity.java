package com.view.widget.label.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.view.widget.label.WidgetFlow;
import com.view.widget.label.sample.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.StrConstant;

public class WidgetFlowActivity extends BaseAppCompatActivity
{
	private TabLayout tabLayout;
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, WidgetFlowActivity.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_widget_flow);

		final WidgetFlow widgetFlow = new WidgetFlow(this, R.id.flow_label){
			@Override
			protected int getItemResourceId()
			{
				return super.getItemResourceId();
			}
		};
		widgetFlow.addDataAll(StrConstant.getListThree(10));

		tabLayout = (TabLayout) findViewById(R.id.tab_label_flow);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				switch (tab.getPosition())
				{
					case 0:
						widgetFlow.addData(StrConstant.getStringByRandom(StrConstant.getStringArrayByRandom()));
						break;
					case 1:
						int length = widgetFlow.getDataSize();
						widgetFlow.removeView(length - 1);
						break;
					case 2:
						InnerShowActivity.actionStart(WidgetFlowActivity.this, R.layout.activity_label_widget_flow_xml);
						break;
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{

			}
		});

		tabLayout.addTab(tabLayout.newTab().setText("Add"));
		tabLayout.addTab(tabLayout.newTab().setText("Remove"));
		tabLayout.addTab(tabLayout.newTab().setText("Xml展示"));
	}
}
