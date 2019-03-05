package com.yline.finger.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

/**
 * 到了AndroidP，FingerprintManager就正式退役了，系统新增了BiometricPrompt接口
 * BiometricPrompt 在 28及以上，实现指纹识别[内部实现]
 * 不处理任何异常，和版本情况
 *
 * @author yline 2019/2/25 -- 16:00
 */
class Finger28Simple {
    private static final String KEY_NAME = "FingerApi28Impl";

    private CancellationSignal mSignal;
    private String mTitle;
    private String mDescription;
    private String mSubTitle;
    private String mNegativeText;
    private DialogInterface.OnClickListener mClickListener;

    public static Finger28Simple from() {
        return new Finger28Simple();
    }

    Finger28Simple() {
    }

    public void authenticate(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            LogUtil.e("Android 版本低");
            return;
        }
        authenticateInner(context);
    }

    /**
     * 三星出现过：java.lang.NoClassDefFoundError问题，BiometricPrompt.Builder找不到
     *
     * @param context 上下文
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private void authenticateInner(Context context) {
        LogUtil.v("开始校验");

        try {
            BiometricPrompt.Builder promptBuilder = new BiometricPrompt.Builder(context);
            promptBuilder.setTitle(TextUtils.isEmpty(mTitle) ? "fingerprint identification" : mTitle);
            promptBuilder.setDescription(TextUtils.isEmpty(mDescription) ? "" : mDescription);
            promptBuilder.setNegativeButton(TextUtils.isEmpty(mNegativeText) ? "cancel" : mNegativeText, context.getMainExecutor(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != mClickListener) {
                        mClickListener.onClick(dialog, which);
                    }
                }
            });
            BiometricPrompt prompt = promptBuilder.build();

            mSignal = new CancellationSignal();
            prompt.authenticate(providerCryptoObject(), mSignal, context.getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    SDKManager.toast("校验Error");
                    LogUtil.v("errorCode = " + errorCode + ", errString = " + errString);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    SDKManager.toast("校验Failed");
                    LogUtil.v("");
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                    SDKManager.toast("校验help");
                    LogUtil.v("helpCode = " + helpCode + ", helpString = " + helpString);
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    LogUtil.v("");
                    SDKManager.toast("验证成功");
                    if (null != mSignal) {
                        mSignal.cancel();
                    }
                }
            });
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    void setTitle(String title) {
        this.mTitle = title;
    }

    void setNegativeText(String negativeText) {
        this.mNegativeText = negativeText;
    }

    /**
     * 别人的创建方式如此
     *
     * @return BiometricPrompt.CryptoObject or null
     * @throws Exception 异常
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private BiometricPrompt.CryptoObject providerCryptoObject() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_SIGN);
        builder.setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"));
        builder.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
        // Require the user to authenticate with a biometric to authorize every use of the key
        builder.setUserAuthenticationRequired(true);
        builder.setInvalidatedByBiometricEnrollment(true);
        generator.initialize(builder.build());

        KeyPair keyPair = generator.genKeyPair();
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(keyPair.getPrivate());
        return new BiometricPrompt.CryptoObject(signature);
    }
}
