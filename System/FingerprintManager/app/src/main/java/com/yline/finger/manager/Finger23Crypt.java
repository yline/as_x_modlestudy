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
import com.yline.utils.SPUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
 * 每次获取对称钥，都需要验证指纹，所以加密、解密都只能在本地进行；
 * 同时也能保证，每次获取公私钥都是通过指纹的[但不能保证安全]
 *
 * @author yline 2019/3/4 -- 19:02
 */
public class Finger23Crypt {
    private static final String KEY_NAME = "yline_finger_23_crypt";
    private CancellationSignal mSignal;

    public static Finger23Crypt from() {
        return new Finger23Crypt();
    }

    public void authenticateEncrypt(Context context, final String goodsInfo, final OnEncryptCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.v("Android 版本低");
            return;
        }
        authenticateEncryptInner(context, goodsInfo, callback);
    }

    public void authenticateDecrypt(Context context, final OnDecryptCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.v("Android 版本低");
            return;
        }
        authenticateDecryptInner(context, callback);
    }

    public void cancel() {
        if (null != mSignal) {
            mSignal.cancel();
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void authenticateEncryptInner(Context context, final String goodsInfo, final OnEncryptCallback callback) {
        SDKManager.toast("开始校验");
        LogUtil.v("开始校验");

        mSignal = new CancellationSignal();
        FingerprintManagerCompat.CryptoObject cryptoObject = providerEncryptObject();
        if (null == cryptoObject) {
            SDKManager.toast("秘钥不能重复创建");
            return;
        }

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
                    setEncryptStr(encryptBytes);

                    byte[] vectorBytes = cipher.getIV();
                    setVectorStr(vectorBytes);

                    if (null != callback) {
                        callback.onEncrypt(Finger23Crypt.this);
                    }
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void authenticateDecryptInner(Context context, final OnDecryptCallback callback) {
        SDKManager.toast("开始校验");
        LogUtil.v("开始校验");

        mSignal = new CancellationSignal();

        byte[] vectorBytes = getVectorStr();
        FingerprintManagerCompat.CryptoObject cryptoObject = providerDecryptObject(vectorBytes);
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
                    byte[] encryptBytes = getEncryptStr();
                    byte[] goodsInfo = cipher.doFinal(encryptBytes);

                    if (null != callback) {
                        callback.onDecrypt(Finger23Crypt.this, new String(goodsInfo));
                    }
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static final String KEY_ENCRYPT = "key_encrypt";
    private static final String KEY_ENCRYPT_VECTOR = "key_encrypt_vector";

    private static void setEncryptStr(byte[] encryptBytes) {
        String encryptStr = Base64.encodeToString(encryptBytes, Base64.NO_WRAP);
        LogUtil.v("setEncryptStr = " + encryptStr);
        SPUtil.put(SDKManager.getApplication(), KEY_ENCRYPT, encryptStr);
    }

    public static byte[] getEncryptStr() {
        String encryptStr = (String) SPUtil.get(SDKManager.getApplication(), KEY_ENCRYPT, "");
        LogUtil.v("getEncryptStr = " + encryptStr);
        return (null == encryptStr ? null : Base64.decode(encryptStr, Base64.NO_WRAP));
    }

    private static void setVectorStr(byte[] vectorBytes) {
        String vectorStr = Base64.encodeToString(vectorBytes, Base64.NO_WRAP);
        LogUtil.v("setVectorStr = " + vectorStr);
        SPUtil.put(SDKManager.getApplication(), KEY_ENCRYPT_VECTOR, vectorStr);
    }

    public static byte[] getVectorStr() {
        String vectorStr = (String) SPUtil.get(SDKManager.getApplication(), KEY_ENCRYPT_VECTOR, "");
        LogUtil.v("getVectorStr = " + vectorStr);
        return (null == vectorStr ? null : Base64.decode(vectorStr, Base64.NO_WRAP));
    }

    public interface OnEncryptCallback {
        /**
         */
        void onEncrypt(Finger23Crypt crypt);
    }

    public interface OnDecryptCallback {
        /**
         * @param goodsInfo 加密的内容
         */
        void onDecrypt(Finger23Crypt crypt, String goodsInfo);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private static FingerprintManagerCompat.CryptoObject providerDecryptObject(byte[] vectorBytes) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.isKeyEntry(KEY_NAME)) {
                LogUtil.v("秘钥还未创建！！！");
                return null;
            }

            Key secretKey = keyStore.getKey(KEY_NAME, null);
            if (null == secretKey) {
                LogUtil.v("获取秘钥失败");
                return null;
            } else {
                byte[] secretBytes = secretKey.getEncoded();
                LogUtil.v("secretKey = " + (null == secretBytes ? "null" : Base64.encodeToString(secretBytes, Base64.NO_WRAP)) + ", format = " + secretKey.getAlgorithm());
            }

            // 生成Cipher
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(vectorBytes));
            return new FingerprintManagerCompat.CryptoObject(cipher);
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
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取加密工具
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private static FingerprintManagerCompat.CryptoObject providerEncryptObject() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            // 如果没有，则创建，另命名[KeyName]
            if (!keyStore.isKeyEntry(KEY_NAME)) {
                LogUtil.v("创建 对称秘钥，name = " + KEY_NAME);
                createKeyInner();
            } else {
                // 如果已创建，未经过指纹校验直接init会报 android.security.keystore.KeyPermanentlyInvalidatedException: Key permanently invalidated
                LogUtil.v("秘钥，已经创建过了");
                return null;
            }

            // 获取最终的 key
            Key secretKey = keyStore.getKey(KEY_NAME, null);
            if (null == secretKey) {
                LogUtil.v("获取秘钥失败");
                return null;
            } else {
                byte[] secretBytes = secretKey.getEncoded();
                LogUtil.v("secretKey = " + (null == secretBytes ? "null" : Base64.encodeToString(secretBytes, Base64.NO_WRAP)) + ", format = " + secretKey.getAlgorithm());
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
    private static void createKeyInner() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            specBuilder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            specBuilder.setUserAuthenticationRequired(true); // 每次使用这个秘钥，需要指纹验证

            // true的话，注册新指纹，秘钥将失效；false则不会
            // specBuilder.setInvalidatedByBiometricEnrollment(true); ??? 调研一下这个方法

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
