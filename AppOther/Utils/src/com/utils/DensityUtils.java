package com.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 */
public class DensityUtils
{
    
    public DensityUtils()
    {
        /** 实例化失败 */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * dp to px
     * 
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.getResources().getDisplayMetrics());
    }
    
    /**
     * sp to px
     * 
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            spValue,
            context.getResources().getDisplayMetrics());
    }
    
    /**
     * px to dp
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2dp(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale);
    }
    
    /**
     * px to sp
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2sp(Context context, float pxValue)
    {
        return (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
