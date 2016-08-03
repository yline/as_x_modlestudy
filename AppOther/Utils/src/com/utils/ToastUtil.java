package com.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义 toast 的样式
 * @author YLine
 *
 * 2016年8月3日 下午7:58:22
 */
public class ToastUtil
{
    private static Toast toast;
    
    private static TextView tv;
    
    public static void show(Context context, String msg)
    {
        if (null == toast)
        {
            toast = new Toast(context);
        }
        
        if (null == tv)
        {
            tv = new TextView(context);
            tv.setBackgroundResource(R.drawable.bg_toast);
            tv.setTextColor(0xffffffff);
        }
        
        tv.setText(msg);
        toast.setView(tv);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
