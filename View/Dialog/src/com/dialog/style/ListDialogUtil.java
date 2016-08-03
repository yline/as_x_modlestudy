package com.dialog.style;

import java.util.ArrayList;
import java.util.List;

import com.dialog.MainApplication;
import com.yline.log.LogFileUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 列表显示
 * @author YLine
 *
 * 2016年8月3日 下午8:45:59
 */
public class ListDialogUtil
{
    /**
     * 一般表现为        列表
     * @param title     title 标题
     */
    public static void showDialogSelect(Context context, String title, final String[] strs)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setItems(strs, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "ListDialogUtil -> setItems which = " + strs[which]);
            }
        });
        dialog.create().show();
    }
    
    private static int choice;
    
    /**
     * 一般表现为        列表单选    默认选中第一个
     * @param iconId    icon id
     * @param title     title 标题
     */
    public static void showDialogRadio(Context context, int iconId, String title, final String[] strs,
        final String oKText, final String cancelText)
    {
        choice = 1; // 默认选项
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(iconId);
        dialog.setTitle(title);
        dialog.setSingleChoiceItems(strs, choice, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                choice = which;
                LogFileUtil.v(MainApplication.TAG, "setSingleChoiceItems which = " + which);
            }
        });
        dialog.setPositiveButton(oKText, new DialogInterface.OnClickListener()
        { // 确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "setPositiveButton which = " + which);
            }
        });
        dialog.setNegativeButton(cancelText, new DialogInterface.OnClickListener()
        { // 取消
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "setNegativeButton which = " + which);
            }
        });
        dialog.create().show();
    }
    
    private static List<Integer> multiChoiceId = new ArrayList<Integer>();
    
    /**
     * 一般表现为        列表多选
     * @param iconId    icon id
     * @param title     title 标题
     */
    public static void showtDialogCheck(Context context, int iconId, String title, final String[] strs,
        final String oKText, final String cancelText)
    {
        multiChoiceId.clear();
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(iconId);
        dialog.setTitle(title);
        dialog.setMultiChoiceItems(strs, null, new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {
                if (isChecked)
                {
                    multiChoiceId.add(which);
                    LogFileUtil.v(MainApplication.TAG, "setMultiChoiceItems add which = " + which);
                }
                else
                {
                    multiChoiceId.remove(which);
                    LogFileUtil.v(MainApplication.TAG, "setMultiChoiceItems remove which = " + which);
                }
            }
        });
        dialog.setPositiveButton(oKText, new DialogInterface.OnClickListener()
        { // 确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "setPositiveButton whichs = " + multiChoiceId.toString());
            }
        });
        dialog.setNegativeButton(cancelText, new DialogInterface.OnClickListener()
        { // 取消
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "setNegativeButton whichs = " + multiChoiceId.toString());
            }
        });
        dialog.create().show();
    }
}
