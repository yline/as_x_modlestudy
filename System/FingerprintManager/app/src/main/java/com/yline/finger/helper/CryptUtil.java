package com.yline.finger.helper;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.yline.utils.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

/**
 * AES对称秘钥
 *
 * @author yline 2019/3/11 -- 10:41
 */
public class CryptUtil {
    private static final String AEC_KEY_NAME = "yline_finger_23_crypt";

    /**
     * 获取KeyStore
     *
     * @return null if load failed
     */
    public static KeyStore getKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            return keyStore;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogUtil.e("getKeyStore exception");
        return null;
    }

    /**
     * 从AndroidKeyStore中获取对称Key，如果没有，则说明秘钥不存在
     */
    public static Key getKey(KeyStore keyStore) {
        try {
            return keyStore.getKey(AEC_KEY_NAME, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        LogUtil.e("getKey exception");
        return null;
    }

    /**
     * 创建本地 公私钥
     * 系统自动保存到 AndroidKeyStore中
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public static void createKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(AEC_KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
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

    /**
     * 判断公私钥是否存在
     *
     * @return true(存在)
     */
    public static boolean isKeyExist(@NonNull KeyStore keyStore) {
        try {
            return keyStore.isKeyEntry(AEC_KEY_NAME);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        LogUtil.e("isKeyExist exception");
        return false;
    }

    /**
     * 删除公私钥
     *
     * @return true(公私钥 ， 清除成功)
     */
    public static boolean deleteKey() {
        KeyStore keyStore = getKeyStore();
        if (null == keyStore) {
            LogUtil.e("keystore is null");
            return false;
        }
        return deleteKey(keyStore);
    }

    /**
     * 删除公私钥
     *
     * @return true(公私钥 ， 清除成功)
     */
    public static boolean deleteKey(@NonNull KeyStore keyStore) {
        try {
            if (keyStore.isKeyEntry(AEC_KEY_NAME)) {
                keyStore.deleteEntry(AEC_KEY_NAME);
                LogUtil.v("deleteKey success");
                return true;
            } else {
                LogUtil.e("key is not exist");
                return true;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        LogUtil.e("deleteKey exception");
        return false;
    }
}
