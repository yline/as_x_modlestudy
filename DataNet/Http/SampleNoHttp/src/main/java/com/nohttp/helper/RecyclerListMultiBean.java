package com.nohttp.helper;

public class RecyclerListMultiBean
{
	private String title;

	private String subTitle;

	public RecyclerListMultiBean(String title, String subTitle)
	{
		this.title = title;
		this.subTitle = subTitle;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSubTitle()
	{
		return subTitle;
	}

	public void setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
	}
}
