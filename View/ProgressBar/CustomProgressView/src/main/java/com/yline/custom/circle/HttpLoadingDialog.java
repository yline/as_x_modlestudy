package com.yline.custom.circle;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.yline.custom.R;

public class HttpLoadingDialog {
	private Dialog mDialog;
	
	/**
	 * 展示DialogView
	 */
	public void show(Context context) {
		initDialog(context);
		if (null != mDialog && !mDialog.isShowing()) {
			mDialog.show();
		}
	}
	
	/**
	 * 关闭DialogView
	 */
	public void dismiss() {
		if (null != mDialog && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
	private void initDialog(Context context) {
		if (null == mDialog) {
			mDialog = new Dialog(context, R.style.dialog_default_style);
			mDialog.setCancelable(false); // 点击外部，不可取消
			mDialog.setCanceledOnTouchOutside(false); // 不可点击取消
			mDialog.setContentView(new HttpLoadingView(context));
			
			Window dialogWindow = mDialog.getWindow();
			if (null != dialogWindow) {
				dialogWindow.setGravity(Gravity.CENTER);
			}
		}
	}
}
