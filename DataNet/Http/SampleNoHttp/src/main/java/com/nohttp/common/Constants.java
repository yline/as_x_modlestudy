package com.nohttp.common;

public class Constants
{
	/**
	 * 服务器地址.
	 */
	public static final String SERVER = "http://api.nohttp.net/";

	/**
	 * 各种方法测试。
	 */
	public static final String URL_NOHTTP_METHOD = SERVER + "method";

	/**
	 * 支持304缓存的接口--返回text。
	 */
	public static final String URL_NOHTTP_CACHE_STRING = SERVER + "cache";

	/**
	 * 支持304缓存的接口--返回image。
	 */
	public static final String URL_NOHTTP_CACHE_IMAGE = SERVER + "imageCache";

	/**
	 * 请求图片的接口，支持各种方法。
	 */
	public static final String URL_NOHTTP_IMAGE = SERVER + "image";

	/**
	 * 请求jsonObject接口, 支持各种方法。
	 */
	public static final String URL_NOHTTP_JSONOBJECT = SERVER + "jsonObject";

	/**
	 * 请求jsonArray接口, 支持各种方法。
	 */
	public static final String URL_NOHTTP_JSONARRAY = SERVER + "jsonArray";

	/**
	 * 提交Body到服务器。
	 */
	public static final String URL_NOHTTP_POSTBODY = SERVER + "postBody";

	/**
	 * 重定向接口：百度这里的http开头（不是https）的地址会被重定向两次，正好可以演示多级重定向。
	 */
	public static final String URL_NOHTTP_REDIRECT_BAIDU = "http://www.baidu.com";

	/**
	 * 上传文件接口。
	 */
	public static final String URL_NOHTTP_UPLOAD = SERVER + "upload";

	/**
	 * 下载文件。
	 */
	public static final String[] URL_DOWNLOADS = {
			"http://api.nohttp.net/download/1.apk",
			"http://api.nohttp.net/download/2.apk",
			"http://api.nohttp.net/download/3.apk",
			"http://api.nohttp.net/download/4.apk"
	};

}
