package com.dialog.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.dialog.widget.DialogIosWidget;
import com.view.dialog.R;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestFragment;

public class ButtonDialogFragment extends BaseTestFragment {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("选择 之 两个按钮", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                // dialog.setIcon(R.mipmap.ic_launcher);
                // dialog.setTitle("标题");
                // dialog.setMessage("提示信息A\n提示信息B");
                dialog.setTitle("提示信息A\n提示信息B");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按钮
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("ButtonDialogUtil -> setPositiveButton");
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() // 取消
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("ButtonDialogUtil -> setNegativeButton");
                    }
                });
                dialog.create().show(); // 创建并且显示出来
            }
        });

        addButton("选择 之 三个按钮", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("标题");
                dialog.setMessage("提示信息");
                dialog.setCancelable(false);
                dialog.setPositiveButton("one", new DialogInterface.OnClickListener() { // 1
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("ButtonDialogUtil -> setPositiveButton");
                    }
                });
                dialog.setNeutralButton("two", new DialogInterface.OnClickListener() { // 2
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("ButtonDialogUtil -> setNeutralButton");
                    }
                });
                dialog.setNegativeButton("three", new DialogInterface.OnClickListener() { // 3
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("ButtonDialogUtil -> setNegativeButton");
                    }
                });
                dialog.create().show(); // 创建并且显示出来
            }
        });

        addButton("仿IOS两个按钮 选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogIosWidget iosWidget = new DialogIosWidget(getContext());
                iosWidget.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // TODO
                    }
                });
                iosWidget.show();
            }
        });
    }
}
