package com.yline.finger.helper;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.yline.utils.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Finger23Sign
 *
 * @author yline 2019/3/8 -- 15:15
 */
public class SignUtil {
    private static final String ECC_KEY_NAME = "Yline_Finger_Pay";

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
     * 创建本地 公私钥
     * 系统自动保存到 AndroidKeyStore中
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public static void createKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

            KeyGenParameterSpec.Builder specBuilder = new KeyGenParameterSpec.Builder(ECC_KEY_NAME, KeyProperties.PURPOSE_SIGN);
            specBuilder.setDigests(KeyProperties.DIGEST_SHA256);
            specBuilder.setUserAuthenticationRequired(true); // 系统帮你，永远保存公钥、私钥
            keyPairGenerator.initialize(specBuilder.build());

            keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从AndroidKeyStore中获取PublicKey，如果没有，则说明秘钥不存在
     *
     * @return null if key is not create
     */
    public static PublicKey getPublicKey() {
        KeyStore keyStore = getKeyStore();
        if (null == keyStore) {
            LogUtil.e("keystore is null");
            return null;
        }
        return getPublicKey(keyStore);
    }

    /**
     * 从AndroidKeyStore中获取PublicKey，如果没有，则说明秘钥不存在
     *
     * @return null if key is not create
     */
    public static PublicKey getPublicKey(@NonNull KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(ECC_KEY_NAME);
            if (null == certificate) {
                return null;
            }

            PublicKey publicKey = certificate.getPublicKey();
            if (null == publicKey) {
                return null;
            } else { // 由于需要传输给后台，因此使用X09过滤一遍
                KeyFactory factory = KeyFactory.getInstance(publicKey.getAlgorithm());
                X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
                return factory.generatePublic(spec); // verifyPublicKey
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从AndroidKeyStore中获取PrivateKey，如果没有，则说明秘钥不存在
     *
     * @return null if key is not create
     */
    public static PrivateKey getPrivateKey() {
        KeyStore keyStore = getKeyStore();
        if (null == keyStore) {
            LogUtil.e("keystore is null");
            return null;
        }
        return getPrivateKey(keyStore);
    }

    /**
     * 从AndroidKeyStore中获取PrivateKey，如果没有，则说明秘钥不存在
     *
     * @return null if key is not create
     */
    public static PrivateKey getPrivateKey(@NonNull KeyStore keyStore) {
        try {
            return (PrivateKey) keyStore.getKey(ECC_KEY_NAME, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断公私钥是否存在
     *
     * @return true(存在)
     */
    public static boolean isKeyExist() {
        KeyStore keyStore = getKeyStore();
        if (null == keyStore) {
            LogUtil.e("keystore is null");
            return false;
        }
        return isKeyExist(keyStore);
    }

    /**
     * 判断公私钥是否存在
     *
     * @return true(存在)
     */
    public static boolean isKeyExist(@NonNull KeyStore keyStore) {
        try {
            return keyStore.isKeyEntry(ECC_KEY_NAME);
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
            if (keyStore.isKeyEntry(ECC_KEY_NAME)) {
                keyStore.deleteEntry(ECC_KEY_NAME);
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
