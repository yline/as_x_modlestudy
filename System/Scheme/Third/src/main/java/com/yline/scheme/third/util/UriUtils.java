package com.yline.scheme.third.util;

import android.net.Uri;

import com.yline.utils.LogUtil;

public class UriUtils {
	/**
	 * 解析一个 Scheme
	 * [scheme]://[host]:[port][path]?[query]
	 *
	 * @param uri https://blog.csdn.net/search?locationNum=1&fps=1
	 */
	public static void schemePrint(Uri uri) {
		if (null == uri) {
			LogUtil.v("uri is null");
			return;
		}
		
		LogUtil.v("uri = " + uri.toString(), LogUtil.LOG_LOCATION_PARENT); // 【https://blog.csdn.net/search?locationNum=1&fps=1】
		LogUtil.v("scheme = " + uri.getScheme(), LogUtil.LOG_LOCATION_PARENT); // 【https】
		LogUtil.v("host = " + uri.getHost(), LogUtil.LOG_LOCATION_PARENT); // 【blog.csdn.net】
		LogUtil.v("port = " + uri.getPort(), LogUtil.LOG_LOCATION_PARENT); // 【-1】
		LogUtil.v("path = " + uri.getPath(), LogUtil.LOG_LOCATION_PARENT); // 【/search】
		LogUtil.v("query = " + uri.getQuery(), LogUtil.LOG_LOCATION_PARENT); // 【locationNum=1&fps=1】
		LogUtil.v("authority = " + uri.getAuthority(), LogUtil.LOG_LOCATION_PARENT); // 【blog.csdn.net】
	}
}
