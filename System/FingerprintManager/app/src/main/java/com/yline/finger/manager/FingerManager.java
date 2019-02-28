package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;

import com.yline.utils.LogUtil;

/**
 *
 *
 *
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.v("Android 版本低");
            return false;
        }

        if (!helper.isSupport(context)) {
            LogUtil.v("手机硬件不支持");
            return false;
        }

        if (!helper.hasEnrolledFinger(context)) {
            LogUtil.v("手机还未录入指纹");
            return false;
        }

        if (!helper.isKeyguardSecure(context)) {
            LogUtil.v("手机屏幕未打开");
            return false;
        }

        return true;
    }

    /**
     * 是否硬件保障安全
     *
     * @return true(是的)
     */
    public static boolean isSecureByHardware() {
        FingerKeyStore keyStore = new FingerKeyStore();
        return keyStore.isKeyProtectByHardware();
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

    public static void authenticate23Add(Context context, String sourceData, OnAuthCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerApi23Impl fingerApi23 = new FingerApi23Impl();
            fingerApi23.setOnAuthCallback(callback);
            fingerApi23.authenticateAdd(context, sourceData);
        } else {
            LogUtil.e("authenticate23, SDK_INT = " + Build.VERSION.SDK_INT);
        }
    }

    public static void authenticate23Verify(Context context, String encryptData, String initVectorStr, OnAuthCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerApi23Impl fingerApi23 = new FingerApi23Impl();
            fingerApi23.setOnAuthCallback(callback);
            fingerApi23.authenticateVerify(context, encryptData, initVectorStr);
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
