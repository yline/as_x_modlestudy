package com.yline.finger.manager;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * 指纹识别，小工具帮助
 *
 * @author yline 2019/2/25 -- 13:32
 */
class FingerHelper {
    private FingerprintManagerCompat mManagerCompat;

    FingerHelper() {
    }

    /**
     * 硬件，是否支持，指纹识别
     *
     * @param context 上下文
     * @return true(支持)
     */
    boolean isSupport(Context context) {
        try {
            return getFingerprintManagerCompat(context).isHardwareDetected();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 是否录入指纹；
     * PS：有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
     *
     * @param context 上下文
     * @return true(有录入指纹)
     */
    boolean hasEnrolledFinger(Context context) {
        try {
            return getFingerprintManagerCompat(context).hasEnrolledFingerprints();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 判断系统有没有设置锁屏
     *
     * @return true(设置了)
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    boolean isKeyguardSecure(Context context) {
        try {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            return (null != keyguardManager && keyguardManager.isKeyguardSecure());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private FingerprintManagerCompat getFingerprintManagerCompat(Context context) {
        if (null == mManagerCompat) {
            synchronized (FingerprintManagerCompat.class) {
                mManagerCompat = FingerprintManagerCompat.from(context);
            }
        }
        return mManagerCompat;
    }

    /**
     * 打开指纹识别，系统设置界面；[各个厂商，差异太大，统一跳到设置界面完事]
     *
     * @param context 上下文
     * @return true(打开成功)
     */
    public static boolean openFingerSetting(Context context) {
        Intent intent = new Intent("android.settings.SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (null != intent.resolveActivity(context.getPackageManager())) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
