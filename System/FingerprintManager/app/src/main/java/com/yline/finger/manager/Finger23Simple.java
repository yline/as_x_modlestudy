package com.yline.finger.manager;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;

/**
 * 指纹校验，API23，无加密、无签名
 *
 * @author yline 2019/3/4 -- 18:08
 */
public class Finger23Simple {
    public static Finger23Simple from() {
        return new Finger23Simple();
    }

    /**
     * 验证
     */
    public void authenticate(Context context) {
        authenticateInner(context);
    }

    private void authenticateInner(Context context) {
        SDKManager.toast("开始校验");
        LogUtil.v("开始校验");
        FingerCompat.from().authenticate(context, null, null, new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                SDKManager.toast("校验Error");
                LogUtil.v("errMsgId = " + errMsgId + ", errString = " + errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                SDKManager.toast("校验help");
                LogUtil.v("helpMsgId = " + helpMsgId + ", helpString = " + helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                SDKManager.toast("校验Failed");
                LogUtil.v("");
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                LogUtil.v("");
                SDKManager.toast("验证成功");
            }
        });
    }
}
