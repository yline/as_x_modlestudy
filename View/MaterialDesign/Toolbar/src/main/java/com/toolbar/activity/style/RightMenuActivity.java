package com.toolbar.activity.style;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.toolbar.R;
import com.toolbar.activity.MainApplication;

public class RightMenuActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_right_menu);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_right_menu);
		// setSupportActionBar(toolbar);  这句话问题最多!!! 去掉这句话就 ko标题栏了

		toolbar.setNavigationIcon(R.mipmap.ic_launcher);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		toolbar.inflateMenu(R.menu.right_menu);
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				int menuItemId = item.getItemId();
				if (menuItemId == R.id.action_write)
				{
					MainApplication.toast("走人，写任务去");
				}
				return true;
			}
		});

	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, RightMenuActivity.class));
	}
}
