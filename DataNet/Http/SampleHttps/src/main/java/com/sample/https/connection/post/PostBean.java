package com.sample.https.connection.post;

public class PostBean
{
	private int id;

	private String name;

	private String number;

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

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}
	
	@Override
	public String toString()
	{
		return "PostTestBean [id=" + id + ", name=" + name + ", number=" + number + "]";
	}

}
