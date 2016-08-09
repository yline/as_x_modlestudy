package com.listview.common;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListView;

import com.listview.MainApplication;
import com.view.listview.R;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

public class CommonListAdapterInstance
{
	public void show(Context context, ListView lv)
	{
		List<TestBean> list = new ArrayList<>();

		list.add(new TestBean(1, "云彩"));
		list.add(new TestBean(2, "地球"));
		list.add(new TestBean(3, "阳光"));
		list.add(new TestBean(4, "树木"));
		list.add(new TestBean(5, "天空"));
		list.add(new TestBean(6, "群山"));
		list.add(new TestBean(7, "群山"));
		list.add(new TestBean(8, "群山"));
		list.add(new TestBean(6, "群山"));

		lv.setAdapter(new TestCommonAdapter(context, list));

		LogFileUtil.v(MainApplication.TAG, "CommonListAdapterInstance show end");
	}

	private class TestCommonAdapter extends CommonListAdapter<TestBean>
	{
		public TestCommonAdapter(Context context, List<TestBean> list)
		{
			super(context, list);
		}

		@Override
		protected int getItemRes()
		{
			return R.layout.listview_item;
		}

		@Override
		protected void setViewContent(int position, ViewGroup parent, ViewHolder item)
		{
			item.setText(R.id.tv_id, sList.get(position).getId() + "");
			item.setText(R.id.tv_name, sList.get(position).getName());
		}
	}

	private class TestBean
	{
		private int id;

		private String name;

		public TestBean(int id, String name)
		{
			this.id = id;
			this.name = name;
		}

		public int getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

	}
}
