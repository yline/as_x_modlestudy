package com.view.widget.label.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.view.widget.label.sample.R;
import com.view.widget.label.widget.LabelView;
import com.yline.base.BaseAppCompatActivity;

public class WidgetFlowSingleActivity extends BaseAppCompatActivity
{
	private LabelView labelClickView;

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, WidgetFlowSingleActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_widget_flow_single);

		labelClickView = (LabelView) findViewById(R.id.label_click);
		labelClickView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				labelClickView.setChecked(!labelClickView.isChecked());
			}
		});
	}
}
