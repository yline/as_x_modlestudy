package com.dialog.style;

import com.dialog.MainApplication;
import com.dialog.R;
import com.yline.log.LogFileUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CustomDialogUtil
{
    /**
     * 仅显示信息
     * 需要点击其他地方 取消 
     */
    public static void showDialogHint(Context context, String msgText)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(msgText);
        dialog.create().show();
    }
    
    /**
     * 自定义dialog
     */
    public static void showDialogDesign(Context context, int iconId, String title, boolean cancelable,
        final String oKText, final String cancelText)
    {
        final View mDisignDialogContainer = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
        
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(iconId);
        dialog.setTitle(title);
        dialog.setView(mDisignDialogContainer); // 加载视图
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton(oKText, new DialogInterface.OnClickListener()
        { // 确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "showDialogDesign setPositiveButton which = " + which); // 点击后的效果
                
                EditText et_name = (EditText)mDisignDialogContainer.findViewById(R.id.et_dialogdesign_name);
                EditText et_password = (EditText)mDisignDialogContainer.findViewById(R.id.et_dialogdesign_password);
                
                LogFileUtil.v(MainApplication.TAG,
                    "showDialogDesign -> et_name = " + et_name.getText().toString().trim());
                LogFileUtil.v(MainApplication.TAG,
                    "showDialogDesign -> et_password = " + et_password.getText().toString().trim());
            }
        });
        dialog.setNegativeButton(cancelText, new DialogInterface.OnClickListener()
        { // 取消
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                LogFileUtil.v(MainApplication.TAG, "showDialogDesign which = " + which); // 点击后的效果
            }
        });
        dialog.create().show(); // 创建并且显示出来
    }
}
