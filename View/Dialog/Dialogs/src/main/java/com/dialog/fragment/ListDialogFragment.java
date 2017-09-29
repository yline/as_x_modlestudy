package com.dialog.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.view.dialog.R;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestFragment;

import java.util.ArrayList;
import java.util.List;

public class ListDialogFragment extends BaseTestFragment {
    private int choice;

    private List<Integer> multiChoiceId = new ArrayList<>();

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("列表 之 选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] strs = {"list1", "list2", "list3", "list4"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("标题");
                dialog.setItems(strs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("ListDialogUtil -> setItems which = " + strs[which]);
                    }
                });
                dialog.create().show();
            }
        });

        addButton("列表 之 单选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strs1 = {"list11", "list12", "list13", "list14"};

                choice = 1; // 默认选项
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("单选标题");
                dialog.setSingleChoiceItems(strs1, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                        LogFileUtil.v("setSingleChoiceItems which = " + which);
                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("setPositiveButton which = " + which);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 取消
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("setNegativeButton which = " + which);
                    }
                });
                dialog.create().show();
            }
        });

        addButton("列表 之 多选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strs2 = {"list21", "list22", "list23", "list24"};

                multiChoiceId.clear();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("多选标题");
                dialog.setMultiChoiceItems(strs2, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            multiChoiceId.add(which);
                            LogFileUtil.v("setMultiChoiceItems add which = " + which);
                        } else {
                            multiChoiceId.remove(which);
                            LogFileUtil.v("setMultiChoiceItems remove which = " + which);
                        }
                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() { // 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("setPositiveButton whichs = " + multiChoiceId.toString());
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { // 取消
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("setNegativeButton whichs = " + multiChoiceId.toString());
                    }
                });
                dialog.create().show();
            }
        });
    }
}
