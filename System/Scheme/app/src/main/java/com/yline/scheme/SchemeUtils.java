package com.yline.scheme;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import com.yline.utils.LogUtil;

import java.util.List;

public class SchemeUtils {
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
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
			boolean isExist = attachComponentName(context, intent);
			if (isExist) {
				context.startService(intent);
			} else {
				LogUtil.v(routerUrl + " is not exist");
			}
		} else {
			context.startService(intent);
		}
	}
	
	/**
	 * 如果不加，6.0以上报错：
	 * Service Intent must be explicit
	 */
	private static boolean attachComponentName(Context context, Intent intent) {
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
			return true;
		} else {
			return false;
		}
	}
	
	public static void startReceiver(Context context, String attach) {
		String routerUrl = "receiver://com.yline" + attach;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(routerUrl));
		context.sendBroadcast(intent);
	}
}
