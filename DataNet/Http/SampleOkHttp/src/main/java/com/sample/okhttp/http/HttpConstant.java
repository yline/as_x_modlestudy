package com.sample.okhttp.http;

public class HttpConstant
{
	// 是否 输出日志
	private static boolean isDefaultDebug = true;

	public static boolean isDefaultDebug()
	{
		return isDefaultDebug;
	}

	public static void setIsDefaultDebug(boolean isDefaultDebug)
	{
		HttpConstant.isDefaultDebug = isDefaultDebug;
	}
}
