package com.yline.scheme.third.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.yline.utils.LogUtil;

import java.util.List;

public class Utils {
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
		
		String scheme = uri.getScheme();
		String host = uri.getHost();
		int port = uri.getPort();
		String path = uri.getPath();
		String query = uri.getQuery();
		String authority = uri.getAuthority();
		LogUtil.v("uri = " + uri.toString()); // 【https://blog.csdn.net/search?locationNum=1&fps=1】
		LogUtil.v("scheme = " + scheme); // 【https】
		LogUtil.v("host = " + host); // 【blog.csdn.net】
		LogUtil.v("port = " + port); // 【-1】
		LogUtil.v("path = " + path); // 【/search】
		LogUtil.v("query = " + query); // 【locationNum=1&fps=1】
		LogUtil.v("authority = " + authority); // 【blog.csdn.net】
	}
	
	public static void start(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("xl://goods:8888/goodsDetail?goodsId=10011002"));
		context.startActivity(intent);
	}
	
	public static void isValid(Context context) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("xl://goods:8888/goodsDetail?goodsId=10011002"));
		List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		boolean isValid = !activities.isEmpty();
		if (isValid) {
			context.startActivity(intent);
		}
	}
}
