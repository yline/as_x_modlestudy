package com.dialog.style;

import com.dialog.MainApplication;
import com.yline.log.LogFileUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 按钮选择
 * @author YLine
 *
 * 2016年8月3日 下午8:45:50
 */
public class ButtonDialogUtil
{
    /**
     * 一般表现为        确定取消按钮
     * @param iconId    icon id
     * @param title     title 标题
     * @param message   提示信息
     * @param isBack    是否返回按钮 可以取消对话框
     */
    public static void showDialogTwo(Context context, int iconId, String title, String message, boolean cancelable,
        final String oKText, final String cancelText)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        
        dialog.setIcon(iconId);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton(oKText, new DialogInterface.OnClickListener()
        { // 确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "ButtonDialogUtil -> setPositiveButton");
            }
        });
        dialog.setNegativeButton(cancelText, new DialogInterface.OnClickListener()
        { // 取消
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "ButtonDialogUtil -> setNegativeButton");
            }
        });
        dialog.create().show(); // 创建并且显示出来
    }
    
    /**
     * 一般表现为        三个选择按钮
     * @param iconId    icon id
     * @param title     title 标题
     * @param message   提示信息
     * @param isBack    是否返回按钮 可以取消对话框
     */
    public static void showDialogThree(Context context, int iconId, String title, String message, boolean cancelable,
        final String oneText, final String twoText, final String threeText)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(iconId);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton(oneText, new DialogInterface.OnClickListener()
        { // 1
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "ButtonDialogUtil -> setPositiveButton");
            }
        });
        dialog.setNeutralButton(twoText, new DialogInterface.OnClickListener()
        { // 2
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "ButtonDialogUtil -> setNeutralButton");
            }
        });
        dialog.setNegativeButton(threeText, new DialogInterface.OnClickListener()
        { // 3
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "ButtonDialogUtil -> setNegativeButton");
            }
        });
        dialog.create().show(); // 创建并且显示出来
    }
}
