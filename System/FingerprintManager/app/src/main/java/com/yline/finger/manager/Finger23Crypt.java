package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Base64;

import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * 指纹校验，无签名、有加密、有取消
 *
 * @author yline 2019/3/4 -- 19:02
 */
public class Finger23Crypt {
    private static final String KEY_NAME = "yline_finger_23_crypt";
    private CancellationSignal mSignal;

    public static Finger23Crypt from() {
        return new Finger23Crypt();
    }

    public void authenticate(Context context, final String goodsInfo, final OnCryptCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.v("Android 版本低");
            return;
        }
        authenticateInner(context, goodsInfo, callback);
    }

    public void cancel() {
        if (null != mSignal) {
            mSignal.cancel();
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void authenticateInner(Context context, final String goodsInfo, final OnCryptCallback callback) {
        SDKManager.toast("开始校验");
        LogUtil.v("开始校验");

        mSignal = new CancellationSignal();
        FingerprintManagerCompat.CryptoObject cryptoObject = providerCryptoObject();
        FingerCompat.from().authenticate(context, cryptoObject, mSignal, new FingerprintManagerCompat.AuthenticationCallback() {
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

                // 加密工具；就是传递过来的
                Cipher cipher = result.getCryptoObject().getCipher();
                if (null == cipher) {
                    LogUtil.v("cipher is null");
                    return;
                }

                try {
                    byte[] encryptBytes = cipher.doFinal(goodsInfo.getBytes());
                    String encryptStr = Base64.encodeToString(encryptBytes, Base64.NO_WRAP);

                    byte[] vectorBytes = cipher.getIV();
                    String vectorStr = Base64.encodeToString(vectorBytes, Base64.NO_WRAP);

                    if (null != callback) {
                        callback.onEncrypt(Finger23Crypt.this, encryptStr, vectorStr);
                    }
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnCryptCallback {
        /**
         * 直接一次性
         * 传递私钥上去，然后解密
         */
        void onEncrypt(Finger23Crypt crypt, String encryptStr, String vectorStr);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public static String doDecrypt(String encryptStr, String vectorStr) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.isKeyEntry(KEY_NAME)) {
                LogUtil.v("对称钥获取失败");
                return null;
            }
            Key secretKey = keyStore.getKey(KEY_NAME, null);

            byte[] encryptBytes = Base64.decode(encryptStr, Base64.NO_WRAP);
            byte[] vectorBytes = Base64.decode(vectorStr, Base64.NO_WRAP);

            // 生成Cipher
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(vectorBytes));
            byte[] goodsInfo = cipher.doFinal(encryptBytes);
            return new String(goodsInfo);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private FingerprintManagerCompat.CryptoObject providerCryptoObject() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            // 如果没有，则创建，另命名[KeyName]
            if (!keyStore.isKeyEntry(KEY_NAME)) {
                LogUtil.v("创建 对称秘钥，name = " + KEY_NAME);
                createKeyInner();
            }

            // 获取最终的 key
            Key secretKey = keyStore.getKey(KEY_NAME, null);
            if (null == secretKey) {
                LogUtil.v("私钥创建失败");
                return null;
            }

            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new FingerprintManagerCompat.CryptoObject(cipher);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        LogUtil.e("CryptoObject 创建失败");
        return null;
    }

    /**
     * 创建 AES为对称的[未校验是否存在]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private void createKeyInner() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            specBuilder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            specBuilder.setUserAuthenticationRequired(true);
            keyGenerator.init(specBuilder.build());

            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }
}
