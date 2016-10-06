package com.listview.columns.instance;

import android.content.Context;
import android.widget.ListView;

import com.listview.MainApplication;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yline on 2016/10/6.
 */
public class ColumnsAdapterInstance
{
	public void show(Context context, ListView lv, int column)
	{
		List<TestBean> list = new ArrayList<>();

		list.add(new TestBean(1, "云彩"));
		list.add(new TestBean(2, "地球"));
		list.add(new TestBean(3, "阳光"));
		list.add(new TestBean(4, "树木"));
		list.add(new TestBean(5, "天空"));
		list.add(new TestBean(6, "群山"));
		list.add(new TestBean(7, "童话"));
		list.add(new TestBean(8, "回归"));
		list.add(new TestBean(9, "明月"));
		list.add(new TestBean(10, "皎洁"));
		list.add(new TestBean(11, "妖冶"));

		ColumnsAdapter columnAdapter = new ColumnsAdapter(context, column);
		if (2 == column || 3 == column)
		{
			lv.setDividerHeight(0);  // 去掉分割线
			lv.setSelector(android.R.color.transparent);    // 去掉点击效果
		}
		lv.setAdapter(columnAdapter);
		columnAdapter.addAll(list);

		LogFileUtil.v(MainApplication.TAG, "ColumnsAdapterInstance show cloumn = " + column);
	}
}
