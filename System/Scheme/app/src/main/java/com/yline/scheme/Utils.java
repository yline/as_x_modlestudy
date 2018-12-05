package com.yline.scheme;

import android.net.Uri;

import com.yline.utils.LogUtil;

public class Utils {
	public static void schemePrint(Uri uri) {
		if (null == uri) {
			LogUtil.v("uri is null");
			return;
		}
		
		String scheme = uri.getScheme();
		String host = uri.getHost();
		int port = uri.getPort();
		String path = uri.getPath();
		String query = uri.getQuery();
		String authority = uri.getAuthority();
		LogUtil.v("uri = " + uri.toString());
		LogUtil.v("scheme = " + scheme);
		LogUtil.v("host = " + host);
		LogUtil.v("port = " + port);
		LogUtil.v("path = " + path);
		LogUtil.v("query = " + query);
		LogUtil.v("authority = " + authority);
	}
}
