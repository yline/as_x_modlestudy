package com.sqlite.query.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sqlite.query.R;
import com.sqlite.query.bean.Person;
import com.sqlite.query.helper.QueryDbManager;
import com.yline.base.BaseAppCompatActivity;

import java.util.List;

public class MainActivity extends BaseAppCompatActivity
{
	private TextView tvQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		QueryDbManager.getInstance().insert(30);

		tvQuery = (TextView) findViewById(R.id.tv_query);

		findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				List<Person> list = QueryDbManager.getInstance().queryAll();

				StringBuffer stringBuffer = new StringBuffer();
				for (Person person : list)
				{
					stringBuffer.append(person.toString() + "\n");
				}

				tvQuery.setText(stringBuffer.toString());
			}
		});

		findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				tvQuery.setText("null");
			}
		});

		findViewById(R.id.btn_query_api).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				List<Person> list = QueryDbManager.getInstance().queryAllByApi();

				StringBuffer stringBuffer = new StringBuffer();
				for (Person person : list)
				{
					stringBuffer.append(person.toString() + "\n");
				}

				tvQuery.setText(stringBuffer.toString());
			}
		});
	}
	
}
