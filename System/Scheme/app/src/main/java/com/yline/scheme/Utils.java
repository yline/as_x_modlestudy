package com.yline.scheme;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.yline.utils.LogUtil;

import java.util.List;

public class Utils {
	public static void startActivity(Context context, String attach) {
		String routerUrl = "activity://com.yline" + attach;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(routerUrl));
		if (null != intent.resolveActivity(context.getPackageManager())) {
			context.startActivity(intent);
		} else {
			LogUtil.v(routerUrl + " is not exist");
		}
	}
	
	public static void startService(Context context, String attach) {
		String routerUrl = "service://com.yline" + attach;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(routerUrl));
		
		ComponentName componentName = null;
		PackageManager packageManager = context.getPackageManager();
		if (null != packageManager) {
			ResolveInfo resolveInfo = packageManager.resolveService(intent, 0);
			if (null != resolveInfo) {
				String packageName = resolveInfo.serviceInfo.packageName;
				String className = resolveInfo.serviceInfo.name;
				componentName = new ComponentName(packageName, className);
			}
		}
		
		if (null != componentName) {
			intent.setComponent(componentName);
			context.startService(intent);
		} else {
			LogUtil.v(routerUrl + " is not exist");
		}
	}
	
	/**
	 * 如果不加，6.0以上报错：
	 * Service Intent must be explicit
	 */
	private void attachComponentName(Intent intent) {
	
	}
	
	public static void startReceiver(Context context, String attach) {
		String routerUrl = "receiver://com.yline" + attach;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(routerUrl));
		context.sendBroadcast(intent);
	}
}
