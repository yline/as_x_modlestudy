package com.utils;

import com.utils.activity.MainApplication;
import com.yline.log.LogFileUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 获得屏幕相关的辅助类
 */
public class ScreenUtil
{
    
    public ScreenUtil()
    {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 获得屏幕宽度
     * 
     * @param context
     * @return such as 720 if success
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    
    /**
     * 获取当前屏幕的绝对宽度,(排除状态栏、底部栏、横竖屏等因素)
     * @return
     */
    public static int getAbsoluteScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
        {
            try
            {
                widthPixels = (Integer)Display.class.getMethod("getRawWidth").invoke(display);
                heightPixels = (Integer)Display.class.getMethod("getRawHeight").invoke(display);
            }
            catch (Exception ignored)
            {
                LogFileUtil.e(MainApplication.TAG, "ScreenUtil getAbsoluteScreenWidth < 17 Exception", ignored);
            }
        }
        
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
        {
            try
            {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            }
            catch (Exception ignored)
            {
                LogFileUtil.e(MainApplication.TAG, "ScreenUtil getAbsoluteScreenWidth >= 17 Exception", ignored);
            }
        }
        
        return Math.min(widthPixels, heightPixels);
    }
    
    /**
     * 获取当前屏幕的绝对高度,(排除状态栏、底部栏、横竖屏等因素)
     * @return
     */
    public static int getAbsoluteScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
        {
            try
            {
                widthPixels = (Integer)Display.class.getMethod("getRawWidth").invoke(display);
                heightPixels = (Integer)Display.class.getMethod("getRawHeight").invoke(display);
            }
            catch (Exception ignored)
            {
                LogFileUtil.e(MainApplication.TAG, "ScreenUtil getAbsoluteScreenHeight < 17 Exception", ignored);
            }
        }
        
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
        {
            try
            {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            }
            catch (Exception ignored)
            {
                LogFileUtil.e(MainApplication.TAG, "ScreenUtil getAbsoluteScreenHeight >= 17 Exception", ignored);
            }
        }
        
        return Math.max(widthPixels, heightPixels);
    }
    
    /**
     * 获得屏幕高度
     * 
     * @param context
     * @return such as 1184 if success
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    
    /**
     * 获得状态栏高度
     * 
     * @param context
     * @return such as 50 if success
     */
    public static int getStatusHeight(Context context)
    {
        int statusHeight = -1;
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        }
        catch (Exception e)
        {
            LogFileUtil.e(MainApplication.TAG, "ScreenUtil -> getStatusHeight Exception", e);
        }
        return statusHeight;
    }
    
    /**
     * 获取当前屏幕截图，包含状态栏
     * 
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }
    
    /**
     * 获取当前屏幕截图，不包含状态栏
     * 
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
