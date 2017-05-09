package com.coordinator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;

public class RecyclerActivity extends BaseAppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recycler);

		findViewById(R.id.float_recycler).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Snackbar.make(v, "float_recycler", Snackbar.LENGTH_LONG).setAction("cancel", new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//这里的单击事件代表点击消除Action后的响应事件
					}
				}).show();
			}
		});
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, RecyclerActivity.class));
	}
}
