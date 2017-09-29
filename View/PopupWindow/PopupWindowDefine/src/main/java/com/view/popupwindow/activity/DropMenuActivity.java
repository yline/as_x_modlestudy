package com.view.popupwindow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.view.popupwindow.R;
import com.view.popupwindow.widget.DropMenuWidget;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.test.StrConstant;

import java.util.ArrayList;
import java.util.List;

public class DropMenuActivity extends BaseAppCompatActivity
{
	public static void launcher(Context context)
	{
		if (null != context)
		{
			Intent intent = new Intent(context, DropMenuActivity.class);
			if (!(context instanceof Activity))
			{
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drop_menu);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_drop_menu);

		List<View> containerViewList = new ArrayList<>();
		containerViewList.add(addTextView("下拉1"));
		containerViewList.add(addTextView("下拉2"));
		containerViewList.add(addTextView("下拉3"));

		DropMenuWidget dropMenuWidget = new DropMenuWidget(this, tabLayout);
		dropMenuWidget.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				LogFileUtil.v("onTabSelected");
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{
				LogFileUtil.v("onTabUnselected");
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{
				LogFileUtil.v("onTabReselected");
			}
		});
		dropMenuWidget.show(StrConstant.getListFour(3), containerViewList);
	}
	
	private View addTextView(String content)
	{
		TextView textView = new TextView(this);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(layoutParams);
		textView.setBackgroundResource(android.R.color.white);
		textView.setText(content);
		return textView;
	}
}
