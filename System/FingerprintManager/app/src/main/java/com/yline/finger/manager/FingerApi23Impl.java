package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import java.security.Key;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * FingerprintManagerCompat 在 23及以上，实现指纹识别[内部实现]
 * 不处理任何异常，和版本情况
 *
 * @author yline 2019/2/25 -- 15:28
 */
class FingerApi23Impl {
    private static final String KEY_NAME = "FingerApi23Impl";
    private KeyStore mKeyStore;
    private CancellationSignal mSignal;
    private OnAuthCallback mOnAuthCallback;

    FingerApi23Impl() {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    void authenticate(Context context) {
        try {
            // 创建需要的变量
            FingerprintManagerCompat.CryptoObject cryptoObject = providerCryptoObject();
            mSignal = new CancellationSignal();

            // 开始认证
            FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context);
            managerCompat.authenticate(cryptoObject, 0, mSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);
                    if (null != mOnAuthCallback) {
                        mOnAuthCallback.onAuthError(errMsgId, errString);
                    }
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    if (null != mOnAuthCallback) {
                        mOnAuthCallback.onAuthFailed();
                    }
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    super.onAuthenticationHelp(helpMsgId, helpString);
                    if (null != mOnAuthCallback) {
                        mOnAuthCallback.onAuthHelp(helpMsgId, helpString);
                    }
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    if (null != mOnAuthCallback) {
                        mOnAuthCallback.onAuthSucceeded();
                    }
                    if (null != mSignal) {
                        mSignal.cancel();
                    }
                }
            }, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setOnAuthCallback(OnAuthCallback mOnAuthCallback) {
        this.mOnAuthCallback = mOnAuthCallback;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private FingerprintManagerCompat.CryptoObject providerCryptoObject() throws Exception {
        // 生成Key
        KeyStore keyStore = getKeyStore(KEY_NAME);
        Key secretKey = keyStore.getKey(KEY_NAME, null);

        // 生成Cipher
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE, secretKey);

        return new FingerprintManagerCompat.CryptoObject(cipher);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private KeyStore getKeyStore(String keyName) throws Exception {
        if (null == mKeyStore) {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        }

        if (!mKeyStore.isKeyEntry(keyName)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            specBuilder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            specBuilder.setUserAuthenticationRequired(true);
            keyGenerator.init(specBuilder.build());
            keyGenerator.generateKey();
        }

        return mKeyStore;
    }
}
