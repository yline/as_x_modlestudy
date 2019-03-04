package com.yline.finger.manager;

import android.content.Context;
import android.content.Intent;

/**
 * 依据测试情况，提供API
 *
 * @author yline 2019/3/4 -- 17:50
 */
public class FingerManager {
    /**
     * 指纹校验，前后有加密、有取消
     */
    public static void authWithCrypt(Context context, final String goodsInfo, final Finger23Crypt.OnCryptCallback callback) {
        Finger23Crypt.from().authenticate(context, goodsInfo, callback);
    }

    /**
     * 指纹简单校验；无签名、无加密、无取消
     */
    public static void authSimple(Context context) {
        Finger23Simple.from().authenticate(context);
    }

    /**
     * 指纹是否开启
     *
     * @param context 上下文
     * @return true(开启)
     */
    public static boolean isFingerEnable(Context context) {
        return FingerCompat.from().isFingerEnable(context);
    }

    /**
     * 是否硬件保障安全
     *
     * @return true(是的)
     */
    public static boolean isSecureByHardware() {
        return KeyStoreCompat.from().isKeyProtectByHardware();
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
