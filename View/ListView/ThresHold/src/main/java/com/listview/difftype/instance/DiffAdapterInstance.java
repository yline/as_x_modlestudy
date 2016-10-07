package com.listview.difftype.instance;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yline on 2016/10/7.
 */
public class DiffAdapterInstance
{
	private DiffAdapter diffAdapter;

	private String[] tempStrs = {"当越靠近越看不完全", "当风景看一遍", "泪,只能转身后说抱歉", "心,在离别前当期秋千", "在爱的回归线", "阳光在手指间", "童话剧情上演"};

	private Random random;

	public void init(Context context, ListView lv)
	{
		List<DiffBean> list = new ArrayList<DiffBean>();

		list.add(new DiffBean("yline", "志向无限远大", true));
		list.add(new DiffBean("f21", "时间似流水", false));
		list.add(new DiffBean("f21", "年轻的心有了白发", false));
		list.add(new DiffBean("yline", "当初的人呐", true));
		list.add(new DiffBean("yline", "你们如今在哪", true));
		list.add(new DiffBean("f21", "转眼已各奔天涯", false));
		list.add(new DiffBean("f21", "独自走在街上", false));
		list.add(new DiffBean("f21", "只看见曾经的晚霞", false));
		list.add(new DiffBean("yline", "年轻的心有了白发", true));
		list.add(new DiffBean("yline", "只是我还不能够释怀", true));
		list.add(new DiffBean("yline", "放不开", true));

		diffAdapter = new DiffAdapter(context);
		lv.setDividerHeight(0);
		lv.setAdapter(diffAdapter);
		diffAdapter.addAll(list);

		random = new Random();
	}

	public void addFrom()
	{
		String tempStr = tempStrs[random.nextInt(tempStrs.length)];
		diffAdapter.add(new DiffBean("yline", tempStr, true));
	}

	public void addTo()
	{
		String tempStr = tempStrs[random.nextInt(tempStrs.length)];
		diffAdapter.add(new DiffBean("f21", tempStr, false));
	}
}
