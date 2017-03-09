package com.sample.okhttp.bean;

/**
 * 最外层 Bean
 *
 * @author yline 2017/3/9 --> 15:24
 * @version 1.0.0
 */
public class VXBean
{
	private int code;

	private String data;

	public VXBean(int code, String data)
	{
		this.code = code;
		this.data = data;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "VXBean{" +
				"code=" + code +
				", data='" + data + '\'' +
				'}';
	}
}
