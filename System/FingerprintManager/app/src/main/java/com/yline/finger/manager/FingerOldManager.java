package com.yline.finger.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.yline.utils.LogUtil;

/**
 * 指纹识别，统一管理类
 *
 * @author yline 2019/2/25 -- 17:07
 */
public class FingerOldManager {
    public static void authenticate23(Context context, OnAuthCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerApi23Impl fingerApi23 = new FingerApi23Impl();
            fingerApi23.setOnAuthCallback(callback);
            fingerApi23.authenticate(context);
        } else {
            LogUtil.e("authenticate23, SDK_INT = " + Build.VERSION.SDK_INT);
        }
    }

    /**
     * 添加指纹
     */
    public static void authenticate23Add(Context context, Finger23Impl.OnEnrollCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Finger23Impl finger23 = new Finger23Impl();
            finger23.authenticateEnroll(context, callback);
        } else {
            LogUtil.e("authenticate23, SDK_INT = " + Build.VERSION.SDK_INT);
        }
    }

    /**
     * 验证指纹
     */
    public static void authenticate23Verify(Context context, String goodsInfo, final Finger23Impl.OnVerifyCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Finger23Impl finger23 = new Finger23Impl();
            finger23.authenticateVerify(context, goodsInfo, callback);
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
