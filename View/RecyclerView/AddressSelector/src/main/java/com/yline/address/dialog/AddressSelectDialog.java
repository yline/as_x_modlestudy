package com.yline.address.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yline.address.R;
import com.yline.address.view.RegionView;
import com.yline.utils.UIScreenUtil;

public class AddressSelectDialog {
    private Dialog mDialog;
    private RegionView mRegionView;
    private OnRegionListener mOnRegionListener;

    public AddressSelectDialog(Context context) {
        mDialog = new Dialog(context, R.style.DialogCommonStyle);
        mDialog.setCanceledOnTouchOutside(true);

        mRegionView = new RegionView(context);
        mRegionView.setOnCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 设置item监听，回调传回结果
        mRegionView.setOnLastItemClickListener(new RegionView.OnLastItemClickListener() {
            @Override
            public void onItemClick(String province, String city, String area, String street) {
                if (null != mOnRegionListener) {
                    mOnRegionListener.onRegionListener(province, city, area);
                }
                dismiss();
            }
        });

        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setContentView(mRegionView);
        WindowManager.LayoutParams attributes = window.getAttributes();

        attributes.width = UIScreenUtil.getScreenWidth(context);// 宽为手机屏幕宽
        attributes.height = UIScreenUtil.getScreenHeight(context) * 4 / 5;// 高为手机屏幕高的4/5
        // window.setBackgroundDrawableResource(R.color.white);
        window.setAttributes(attributes);
        window.setWindowAnimations(R.style.AnimBottom);
    }

    public void show() {
        if (null != mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void setData(String province, String city, String area, String street) {
        if (null != mRegionView) {
            mRegionView.setData(province, city, area, street);
        }
    }

    public void setOnRegionListener(OnRegionListener listener) {
        this.mOnRegionListener = listener;
    }

    public interface OnRegionListener {
        void onRegionListener(String province, String city, String area);
    }
}
