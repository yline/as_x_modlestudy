package com.gridview.threshold;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class GridViewBean
{
	private int icon;

	private String name;

	public GridViewBean(int icon, String name)
	{
		super();
		this.icon = icon;
		this.name = name;
	}

	public int getIcon()
	{
		return icon;
	}

	public Drawable getIconDrawable(Context context)
	{
		return ContextCompat.getDrawable(context, icon);
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "GridViewBean [icon=" + icon + ", name=" + name + "]";
	}
}
