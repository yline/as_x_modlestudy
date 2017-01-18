package com.sqlite.page.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sqlite.page.R;
import com.sqlite.page.bean.Person;
import com.sqlite.page.helper.DbManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;

public class MainActivity extends BaseAppCompatActivity
{
	private ListView lvLimit;

	private LimitAdapter limitAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DbManager.getInstance().insertAtSameMoment(0, "yline", 21, 60);
		
		initView();
		
		limitAdapter.addAll(DbManager.getInstance().queryAll(26));
	}
	
	private void initView()
	{
		lvLimit = (ListView) findViewById(R.id.lv_limit);
		limitAdapter = new LimitAdapter(this);

		lvLimit.setAdapter(limitAdapter);
	}

	private class LimitAdapter extends CommonListAdapter<Person>
	{
		public LimitAdapter(Context context)
		{
			super(context);
		}

		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_limit;
		}

		@Override
		protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			viewHolder.setText(R.id.tv_id, sList.get(i).getId() + "");
			viewHolder.setText(R.id.tv_name, sList.get(i).getName());
			viewHolder.setText(R.id.tv_age, sList.get(i).getAge() + "");
		}
	}

}
