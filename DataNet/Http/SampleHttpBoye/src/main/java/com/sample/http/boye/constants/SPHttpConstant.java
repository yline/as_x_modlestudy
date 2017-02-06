package com.sample.http.boye.constants;

/**
 * 数据缓存
 */
public class SPHttpConstant
{
	// 这一个sp key的前缀
	private static final String BASE = "http_";

	// token缓存

	/** 超时时间 */
	public static final String EXPIRES_IN = BASE + "expires_in";

	/** 获取token的时间戳 */
	public static final String TIME_STAMP = BASE + "time_stamp";

	/** 获取的token */
	public static final String ACCESS_TOKEN = BASE + "access_token";

}
