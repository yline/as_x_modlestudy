package com.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.view.dialog.R;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestFragment;

public class CustomDialogFragment extends BaseTestFragment {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("自定义 之 提示", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("就是吐丝啊");
                dialog.create().show();
            }
        });

        addButton("自定义 之 no title", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog noTitleDialog = new Dialog(getContext(), android.R.style.Theme_Holo_Dialog_NoActionBar);

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_custom_notitle, null);
                TextView text = (TextView) view.findViewById(R.id.dialog_tv_title);
                text.setText("message");

                noTitleDialog.setContentView(view);
                noTitleDialog.show();
                noTitleDialog.setCanceledOnTouchOutside(true);
            }
        });

        addButton("自定义 之 自定义 view", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View mDisignDialogContainer = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom_design, null);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("标题");
                dialog.setView(mDisignDialogContainer); // 加载视图
                dialog.setCancelable(true);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() { // 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("showDialogDesign setPositiveButton which = " + which); // 点击后的效果

                        EditText et_name = (EditText) mDisignDialogContainer.findViewById(R.id.et_dialogdesign_name);
                        EditText et_password = (EditText) mDisignDialogContainer.findViewById(R.id.et_dialogdesign_password);

                        LogFileUtil.v("showDialogDesign -> et_name = " + et_name.getText().toString().trim());
                        LogFileUtil.v("showDialogDesign -> et_password = " + et_password.getText().toString().trim());
                    }
                });
                dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() { // 取消
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogFileUtil.v("showDialogDesign which = " + which); // 点击后的效果
                    }
                });
                dialog.create().show(); // 创建并且显示出来
            }
        });
    }
}
