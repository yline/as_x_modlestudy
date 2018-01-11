package com.view.wm.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.view.wm.IApplication;
import com.view.wm.R;
import com.view.wm.view.FloatView;
import com.yline.utils.UIScreenUtil;

/**
 * 悬浮框，管理类
 *
 * @author yline 2018/1/11 -- 11:57
 * @version 1.0.0
 */
public class FloatWindowManager {
    private Context mContext;

    private FloatView mFloatView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;

    private View.OnClickListener mOnClickListener;

    public FloatWindowManager(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setOnFloatClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    /**
     * 只考虑展示的情况
     */
    public void updateView() {
        boolean isShow = ("21".contains("1"));
        if (isShow) {
            initWindowView();
        } else {
            if (null != mFloatView && null != mFloatView.getParent()) {
                mWindowManager.removeViewImmediate(mFloatView);
            }
        }
    }

    private void initWindowView() {
        try {
            initFloatView();
            initWindowLayoutParam();

            if (null == mFloatView.getParent()) {
                mWindowManager.addView(mFloatView, mWindowLayoutParams);
            }
        } catch (Exception ex) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean hasPermission = hasPermissionsGranted(IApplication.getApplication(), new String[]{android.Manifest.permission.SYSTEM_ALERT_WINDOW});
                if (!hasPermission) {
                    IApplication.toast("未开通悬浮框权限，请开通");
                }
            }
        }
    }

    private void initFloatView() {
        if (null == mFloatView) {
            mFloatView = new FloatView(mContext);
            mFloatView.setOnUpdatePositionCallback(new FloatView.OnUpdatePositionCallback() {
                @Override
                public void onUpdatePosition(View view, float newX, float newY) {
                    if (null != mWindowManager) {
                        initWindowLayoutParam();
                        mWindowLayoutParams.x = (int) newX;
                        mWindowLayoutParams.y = (int) newY;

                        mWindowManager.updateViewLayout(view, mWindowLayoutParams);
                    }
                }
            });

            mFloatView.setOnViewClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnClickListener) {
                        mOnClickListener.onClick(v);
                    }
                }
            });

            updateAvatarView();
        }
    }

    public void updateAvatarView() {
        if (null != mFloatView) {
            mFloatView.setImageResource(R.drawable.avatar);
        }
    }

    private void initWindowLayoutParam() {
        if (null == mWindowLayoutParams) {
            mWindowLayoutParams = new WindowManager.LayoutParams();

            // TYPE_TOAST : API<=18,无法触发点击事件； api>24, 程序崩溃
            // TYPE_SYSTEM_ALERT; api<24,无需用户授权， api>=24, 需要用户授权
            // TYPE_PHONE: 部分手机可以自适应，但是适配性不太好
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mWindowLayoutParams.format = PixelFormat.RGBA_8888;
            mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mWindowLayoutParams.alpha = 1.0F;

            mWindowLayoutParams.x = UIScreenUtil.getScreenWidth(IApplication.getApplication()) - UIScreenUtil.dp2px(IApplication.getApplication(), 75);
            mWindowLayoutParams.y = UIScreenUtil.getScreenHeight(IApplication.getApplication()) / 2;
        }
    }

    /**
     * 校验是否 权限都通过了
     *
     * @return true{都通过}
     */
    public static boolean hasPermissionsGranted(Context context, String[] permissionList) {
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
