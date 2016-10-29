package com.utils;

import com.utils.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 跟App相关的辅助类
 * @author YLine
 *
 * 2016年7月16日 下午10:08:21
 */
public class AppUtil
{
    
    public AppUtil()
    {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 获取应用程序名称
     * @param context
     * @return null if exception; such as "Utils" if success
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }
        catch (NameNotFoundException e)
        {
            LogFileUtil.e(MainApplication.TAG, "AppUtils -> getAppName NameNotFoundException");
        }
        return null;
    }
    
    /**
     * 获取应用程序版本名称信息
     * @param context
     * @return null if exception; such as "1.0" if success
     */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        }
        catch (NameNotFoundException e)
        {
            LogFileUtil.e(MainApplication.TAG, "AppUtils -> getVersionName NameNotFoundException");
        }
        return null;
    }
}
