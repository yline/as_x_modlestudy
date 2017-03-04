package com.toolbar.activity.style;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.toolbar.R;

public class IconBackActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_icon_back);

		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_icon_back);
		toolbar.setTitle("");
		/*toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});*/

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(false);// 去掉 默认标题;无效
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 开启返回按钮
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, IconBackActivity.class));
	}
}
