package com.dialog.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.dialog.widget.DialogFootWidget;
import com.view.dialog.R;
import com.yline.test.BaseTestFragment;
import com.yline.test.StrConstant;

public class SpecialDialogFragment extends BaseTestFragment {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("底部弹出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFootWidget footWidget = new DialogFootWidget(getContext(), StrConstant.getListEnglish(4));
                footWidget.setOnSelectedListener(new DialogFootWidget.OnSelectedListener() {
                    @Override
                    public void onCancelSelected(DialogInterface dialog) {
                        // TODO
                    }

                    @Override
                    public void onOptionSelected(DialogInterface dialog, int position, String content) {
                        // TODO
                    }
                });
                footWidget.show();
            }
        });

        addButton("全屏", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_special_bottom, null);

                Dialog dialog = new Dialog(getContext(), R.style.dialog);
                dialog.setContentView(view);

                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);

                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.onWindowAttributesChanged(lp);

                dialog.show();
            }
        });

        addButton("input", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatEditText inputTextView = new AppCompatEditText(getContext());

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setView(inputTextView);
                dialogBuilder.setTitle("请输入验证码");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputStr = inputTextView.getText().toString().trim();
                        if ("bdtt".equalsIgnoreCase(inputStr)) {
                            // TODO
                        } else {
                            // TODO
                        }

                        if (null != dialog) {
                            dialog.dismiss();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != dialog) {
                            dialog.dismiss();
                        }
                    }
                });
                dialogBuilder.show();
            }
        });
    }
}
