package com.listview.threshold.instance;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.listview.MainApplication;
import com.view.listview.R;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YLine
 *         <p/>
 *         2016年8月1日 下午10:51:43
 */
public class SimpleAdapterInstance
{
	private static final String ID = "id";

	private static final String NAME = "name";

	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public void setData()
	{
		data.add(getMap("1", "云彩"));
		data.add(getMap("2", "地球"));
		data.add(getMap("3", "阳光"));
		data.add(getMap("4", "树木"));
		data.add(getMap("5", "天空"));
		data.add(getMap("6", "群山"));
	}

	private Map<String, String> getMap(String id, String name)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(ID, id);
		map.put(NAME, name);
		return map;
	}

	public void show(Context context, ListView lv)
	{
		String[] from = new String[]{ID, NAME};
		int[] to = new int[]{R.id.tv_id, R.id.tv_name};

		lv.setAdapter(new SimpleAdapter(context, data, R.layout.listview_item, from, to));
		LogFileUtil.v(MainApplication.TAG, "SimpleAdapterInstance show end");
	}
}
