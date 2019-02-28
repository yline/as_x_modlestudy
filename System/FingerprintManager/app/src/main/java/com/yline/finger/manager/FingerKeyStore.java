package com.yline.finger.manager;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.security.Key;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;

/**
 * keyStore管理类 + FingerPrinterManager
 *
 * @author yline 2019/2/28 -- 16:10
 */
class FingerKeyStore {
    private KeyStore mKeyStore;

    FingerKeyStore() {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    FingerprintManagerCompat.CryptoObject getEncryptObject(String aliasKeyName) throws Exception {
        // 生成Key
        Key secretKey = generateKey(aliasKeyName, null);
        if (null == secretKey) {
            return null;
        }

        // 生成Cipher
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return new FingerprintManagerCompat.CryptoObject(cipher);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    FingerprintManagerCompat.CryptoObject getDecryptObject(String aliasKeyName, byte[] initVector) throws Exception {
        // 生成Key
        Key secretKey = generateKey(aliasKeyName, null);
        if (null == secretKey) {
            return null;
        }

        // 生成Cipher
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(initVector));

        return new FingerprintManagerCompat.CryptoObject(cipher);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    Key getKey(String aliasKeyName) throws Exception {
        return generateKey(aliasKeyName, null);
    }

    /**
     * 校验，秘钥是否被硬件保护
     *
     * @return true(被保护)
     */
    boolean isKeyProtectByHardware() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        } else {
            return false;
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private Key generateKey(String aliasKeyName, char[] pwd) throws Exception {
        if (null == mKeyStore) {
            // AndroidKeyStore 主要存储秘钥Key的，存进该处的key可以设置为KeyProtection
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        }

        if (!mKeyStore.isKeyEntry(aliasKeyName)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(aliasKeyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            specBuilder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            specBuilder.setUserAuthenticationRequired(true);
            keyGenerator.init(specBuilder.build());

            keyGenerator.generateKey();
        }

        return mKeyStore.getKey(aliasKeyName, pwd);
    }
}
