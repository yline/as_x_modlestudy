package com.toolbar.activity.style;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.toolbar.R;
import com.yline.base.BaseAppCompatActivity;

public class TitleCenterActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_style_center_title);

		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setLogo(R.mipmap.ic_launcher); // 图标
		toolbar.setTitle("MainBar"); // 主标题
		// toolbar.setSubtitle("SubBar"); // 副标题

		toolbar.setNavigationIcon(R.mipmap.ic_launcher); // 左边可点击按钮
		// toolbar.setOnMenuItemClickListener();

		setSupportActionBar(toolbar);

	}
}
