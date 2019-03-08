package com.yline.finger.manager;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

/**
 * KeyStore管理类[做签名 + 加密的兼容]
 *
 * @author yline 2019/3/4 -- 17:58
 */
class KeyStoreCompat {
    public static KeyStoreCompat from() {
        return new KeyStoreCompat();
    }

    /**
     * 校验，秘钥是否被硬件保护
     *
     * @return true(被保护)
     */
    public boolean isKeyProtectByHardware() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return isKeyProtectByHardwareInner();
        } else {
            return false;
        }
    }

    /**
     * 校验，秘钥是否被硬件保护
     *
     * @return true(被保护)
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private boolean isKeyProtectByHardwareInner() {
        try {
            SecretKey secretKey = (SecretKey) generateKey("testKey", null);
            if (null == secretKey) {
                return false;
            }

            SecretKeyFactory factory = SecretKeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyInfo keyInfo = (KeyInfo) factory.getKeySpec(secretKey, KeyInfo.class);
            return (null != keyInfo && keyInfo.isInsideSecureHardware() && keyInfo.isUserAuthenticationRequired());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private Key generateKey(String aliasKeyName, char[] pwd) throws Exception {
        // AndroidKeyStore 主要存储秘钥Key的，存进该处的key可以设置为KeyProtection
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // 如果，没有该秘钥，则创建
        if (!keyStore.isKeyEntry(aliasKeyName)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(aliasKeyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            specBuilder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            specBuilder.setUserAuthenticationRequired(true);
            keyGenerator.init(specBuilder.build());

            keyGenerator.generateKey();
        }

        return keyStore.getKey(aliasKeyName, pwd);
    }
}
