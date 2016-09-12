package com.gridview.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.gridview.activity.MainApplication;
import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2016/9/12.
 */
public class DataBean
{
	/** 名称 */
	private String name;

	/** 图标 */
	private Drawable icon;

	public DataBean setName(String name)
	{
		this.name = name;
		return this;
	}

	public String getName()
	{
		return this.name;
	}

	public DataBean setIcon(Context context, int icon)
	{
		this.icon = ContextCompat.getDrawable(context, icon);
		return this;
	}

	public Drawable getIcon()
	{
		return this.icon;
	}

	public void end()
	{
		LogFileUtil.v(MainApplication.TAG, toString());
	}

	@Override
	public String toString()
	{
		return "DataBean{" +
				"name='" + name + '\'' +
				", icon=" + icon +
				'}';
	}
}
