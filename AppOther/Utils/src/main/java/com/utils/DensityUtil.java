package com.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 */
public class DensityUtil
{
    
    public DensityUtil()
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
    
    /**
     * Uri --> path
     * @param context       上下文
     * @param contentUri    content:// 路径
     * @return      String类型的的file路径
     */
    public static String Uri2Path(Context context, Uri imageContentUri)
    {
        String imagePath = "";
        
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, imageContentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        
        int column_index = cursor.getColumnIndex(projection[0]);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);
        
        cursor.close();
        
        return imagePath;
    }
}
