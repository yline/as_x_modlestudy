package com.view.webview.webview.interceptor.file;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.view.webview.R;

/**
 * 相机、相册、取消；三个按钮选择器
 *
 * @author linjiang@kjtpay.com  2019/5/6 -- 18:38
 */
public class PictureSelectDialog {
    private Dialog mScreenFootDialog;
    private View mView;

    public PictureSelectDialog(Context context) {
        this.mView = LayoutInflater.from(context).inflate(R.layout.picture_select_dialog, null);
        this.mScreenFootDialog = new Dialog(context, R.style.dialog_default_style);
        this.mScreenFootDialog.setCancelable(false);
        this.mScreenFootDialog.setCanceledOnTouchOutside(false);

        this.mScreenFootDialog.setContentView(mView);

        initViewClick();
    }

    private void initViewClick() {
    }

    public void show() {
        if (null != mScreenFootDialog) {
            mScreenFootDialog.show();
        }
    }

    public void dismiss() {
        if (null != mScreenFootDialog) {
            mScreenFootDialog.dismiss();
        }
    }

    public void setOnCameraClickListener(View.OnClickListener listener) {
        mView.findViewById(R.id.picture_select_dialog_camera).setOnClickListener(listener);
    }

    public void setOnAlbumClickListener(View.OnClickListener listener) {
        mView.findViewById(R.id.picture_select_dialog_album).setOnClickListener(listener);
    }

    public void setOnFileClickListener(View.OnClickListener listener) {
        mView.findViewById(R.id.picture_select_dialog_file).setOnClickListener(listener);
    }

    public void setOnCancelClickListener(View.OnClickListener listener) {
        mView.findViewById(R.id.picture_select_dialog_cancel).setOnClickListener(listener);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (null != mScreenFootDialog) {
            mScreenFootDialog.setOnDismissListener(listener);
        }
    }
}
