package com.json.gson;

/**
 * Created by yline on 2016/10/6.
 */
public class BaseBean
{
	private int id = -1;

	private String name = "yline";

	public BaseBean()
	{
	}

	public BaseBean(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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
		return "BaseBean{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
