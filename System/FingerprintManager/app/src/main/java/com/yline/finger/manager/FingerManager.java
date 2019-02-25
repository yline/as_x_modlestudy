package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;

import com.yline.utils.LogUtil;

/**
 * 指纹识别，统一管理类
 *
 * @author yline 2019/2/25 -- 17:07
 */
public class FingerManager {
    /**
     * 指纹是否开启
     *
     * @param context 上下文
     * @return true(开启)
     */
    public static boolean isFingerEnable(Context context) {
        FingerHelper helper = new FingerHelper();
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && helper.isSupport(context)
                && helper.hasEnrolledFinger(context) && helper.isKeyguardSecure(context));
    }

    /**
     * 打开指纹识别，系统设置界面；[各个厂商，差异太大，统一跳到设置界面完事]
     *
     * @param context 上下文
     * @return true(打开成功)
     */
    public static boolean openFingerSetting(Context context) {
        return FingerHelper.openFingerSetting(context);
    }

    public static void authenticate23(Context context, OnAuthCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerApi23Impl fingerApi23 = new FingerApi23Impl();
            fingerApi23.setOnAuthCallback(callback);
            fingerApi23.authenticate(context);
        } else {
            LogUtil.e("authenticate23, SDK_INT = " + Build.VERSION.SDK_INT);
        }
    }

    public static void authenticate28(Context context, OnAuthCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            FingerApi28Impl fingerApi28 = new FingerApi28Impl();
            fingerApi28.setOnAuthCallback(callback);
            fingerApi28.authenticate(context);
        } else {
            LogUtil.e("authenticate28, SDK_INT = " + Build.VERSION.SDK_INT);
        }
    }
}
