package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Base64;

import com.yline.application.SDKManager;
import com.yline.finger.helper.CryptUtil;
import com.yline.utils.LogUtil;
import com.yline.utils.SPUtil;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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
    private static final String KEY_ENCRYPT = "key_encrypt";
    private static final String KEY_ENCRYPT_VECTOR = "key_encrypt_vector";

    private static void setEncryptStr(byte[] encryptBytes) {
        String encryptStr = Base64.encodeToString(encryptBytes, Base64.NO_WRAP);
        LogUtil.v("setEncryptStr = " + encryptStr);
        SPUtil.put(SDKManager.getApplication(), KEY_ENCRYPT, encryptStr);
    }

    private static byte[] getEncryptStr() {
        String encryptStr = (String) SPUtil.get(SDKManager.getApplication(), KEY_ENCRYPT, "");
        LogUtil.v("getEncryptStr = " + encryptStr);
        return (TextUtils.isEmpty(encryptStr) ? null : Base64.decode(encryptStr, Base64.NO_WRAP));
    }

    private static void setVectorStr(byte[] vectorBytes) {
        String vectorStr = Base64.encodeToString(vectorBytes, Base64.NO_WRAP);
        LogUtil.v("setVectorStr = " + vectorStr);
        SPUtil.put(SDKManager.getApplication(), KEY_ENCRYPT_VECTOR, vectorStr);
    }

    private static byte[] getVectorStr() {
        String vectorStr = (String) SPUtil.get(SDKManager.getApplication(), KEY_ENCRYPT_VECTOR, "");
        LogUtil.v("getVectorStr = " + vectorStr);
        return (TextUtils.isEmpty(vectorStr) ? null : Base64.decode(vectorStr, Base64.NO_WRAP));
    }

    /**
     * 删除秘钥
     *
     * @return 关闭指纹支付的时候，可以调用
     */
    public static boolean delete() {
        return CryptUtil.deleteKey();
    }

    /**
     * 指纹未开通、秘钥未创建情况【失败情况，暂时不处理】
     *
     * @param value 加密的字符串
     */
    public static void open(Context context, final String value, final OnOpenCallback callback) {
        LogUtil.v("open");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("open failed, version is " + Build.VERSION.SDK_INT + ", smaller than 23");
            return;
        }

        KeyStore keyStore = CryptUtil.getKeyStore();
        if (null == keyStore) {
            LogUtil.e("open failed, keystore is null");
            return;
        }

        if (CryptUtil.isKeyExist(keyStore)) {
            LogUtil.e("open failed, key is existed");
            return;
        }

        // 首次创建，秘钥肯定不会失效
        CryptUtil.createKey();

        Key secretKey = CryptUtil.getKey(keyStore);
        if (null == secretKey) {
            LogUtil.e("open failed, secret key is null");
            return;
        }

        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            FingerCompat.from().authenticate(context, cryptoObject, new CancellationSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);
                    LogUtil.v("errMsgId = " + errMsgId + ", errString = " + errString);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    LogUtil.v("");
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    super.onAuthenticationHelp(helpMsgId, helpString);
                    LogUtil.v("helpMsgId = " + helpMsgId + ", helpString = " + helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    LogUtil.v("");

                    Cipher cipher = result.getCryptoObject().getCipher();
                    if (null == cipher) {
                        LogUtil.e("open failed, cipher is null");
                        return;
                    }

                    try {
                        byte[] encryptBytes = cipher.doFinal(value.getBytes());
                        byte[] vectorBytes = cipher.getIV();

                        setEncryptStr(encryptBytes);
                        setVectorStr(vectorBytes);

                        if (null != callback) {
                            callback.onSuccess();
                        }
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public interface OnOpenCallback {
        void onSuccess();
    }

    private static final int STATE_OPEN = 1;
    private static final int STATE_REOPEN = 2;
    private static final int STATE_SUCCESS = 3;

    /**
     * 指纹未开通，秘钥不存在；则创建秘钥，验证指纹，并要求输入支付密码
     * 指纹已开通，秘钥失效；则验证指纹，并要求输入支付密码
     * 指纹已开通，秘钥有效；直接通过
     */
    public static void verify(Context context, final String value, final OnVerifyCallback callback) {
        LogUtil.v("verify");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("verify failed, version is " + Build.VERSION.SDK_INT + ", smaller than 23");
            return;
        }

        KeyStore keyStore = CryptUtil.getKeyStore();
        if (null == keyStore) {
            LogUtil.e("verify failed, keystore is null");
            return;
        }

        int state = 0;
        FingerprintManagerCompat.CryptoObject cryptoObject = null;
        if (!CryptUtil.isKeyExist(keyStore)) { // 秘钥不存在
            LogUtil.v("verify, key is not exist");

            // 首次创建，秘钥肯定不会失效
            CryptUtil.createKey();

            Cipher cipher = providerCipher(keyStore, Cipher.ENCRYPT_MODE, null);
            if (null == cipher) {
                LogUtil.e("verify failed, first create key failed");
            } else {
                cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
                state = STATE_OPEN; // 秘钥不存在时，创建成功
            }
        } else {
            byte[] vectorBytes = getVectorStr();

            boolean isReopen = true;
            Cipher cipher;
            if (null != vectorBytes) { // 未储存，Vector时，解密直接失败[AES解密要求]
                cipher = providerCipher(keyStore, Cipher.DECRYPT_MODE, vectorBytes);
                if (null != cipher) {
                    cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
                    state = STATE_SUCCESS; // 秘钥存在，并有效
                    isReopen = false;
                }
            }

            // 秘钥存在，但是秘钥失效
            if (isReopen) {
                CryptUtil.deleteKey(keyStore); // 删除秘钥
                CryptUtil.createKey(); // 重新创建秘钥
                cipher = providerCipher(keyStore, Cipher.ENCRYPT_MODE, null);
                if (null == cipher) {
                    LogUtil.e("verify failed, create cipher failed");
                } else {
                    cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
                    state = STATE_REOPEN; // 秘钥存在，但失效；创建秘钥成功
                }
            }
        }

        if (null == cryptoObject) {
            LogUtil.e("verify failed, cryptObject is null");
            return;
        }

        final int finalState = state;
        FingerCompat.from().authenticate(context, cryptoObject, new CancellationSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                LogUtil.v("errMsgId = " + errMsgId + ", errString = " + errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                LogUtil.v("");
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                LogUtil.v("helpMsgId = " + helpMsgId + ", helpString = " + helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                LogUtil.v("finalState = " + finalState);

                Cipher cipher = result.getCryptoObject().getCipher();
                if (null == cipher) {
                    LogUtil.e("open failed, cipher is null");
                    return;
                }

                if (finalState == STATE_OPEN) { // 秘钥不存在，创建
                    try {
                        byte[] encryptBytes = cipher.doFinal(value.getBytes());
                        byte[] vectorBytes = cipher.getIV();

                        setEncryptStr(encryptBytes);
                        setVectorStr(vectorBytes);

                        if (null != callback) {
                            callback.onOpen();
                        }
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                } else if (finalState == STATE_REOPEN) { // 秘钥存在，但是失效，重新创建
                    try {
                        byte[] encryptBytes = cipher.doFinal(value.getBytes());
                        byte[] vectorBytes = cipher.getIV();

                        setEncryptStr(encryptBytes);
                        setVectorStr(vectorBytes);

                        if (null != callback) {
                            callback.onReopen();
                        }
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                } else if (finalState == STATE_SUCCESS) { // 秘钥存在，并有效；对商品解密
                    try {
                        byte[] encryptBytes = getEncryptStr();
                        byte[] valueInfo = cipher.doFinal(encryptBytes);

                        if (null != callback) {
                            callback.onSuccess(new String(valueInfo));
                        }
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface OnVerifyCallback {
        /**
         * 秘钥不存在，创建
         */
        void onOpen();

        /**
         * 秘钥存在，但是失效，重新创建
         */
        void onReopen();

        /**
         * 秘钥存在，并有效；对商品加密
         */
        void onSuccess(String valueInfo);
    }

    /**
     * 获取加密或解密的 Signature
     * 如果返回null，则当做秘钥失效处理
     *
     * @param opmode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @return null if 秘钥失效 或 其他异常
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static Cipher providerCipher(@NonNull KeyStore keyStore, int opmode, byte[] vectorBytes) {
        Key secretKey = CryptUtil.getKey(keyStore);
        if (null == secretKey) {
            LogUtil.e("providerCipher secret key is null");
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (null == vectorBytes) {
                cipher.init(opmode, secretKey);
            } else {
                cipher.init(opmode, secretKey, new IvParameterSpec(vectorBytes));
            }
            return cipher;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        LogUtil.e("providerCipher exception");
        return null;
    }
}
