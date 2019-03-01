package com.yline.finger.manager;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Base64;

import com.yline.finger.MainActivity;
import com.yline.finger.service.HttpUtils;
import com.yline.finger.service.MockService;
import com.yline.utils.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;

/**
 * FingerprintManagerCompat 在 23及以上，实现指纹识别[内部实现]
 * 不处理任何异常，和版本情况
 *
 * @author yline 2019/2/25 -- 15:28
 */
class FingerApi23Impl {
    private static final String ECC_KEY_NAME = "Yline_Finger_Pay";

    private static final String KEY_NAME = "FingerApi23Impl";
    private FingerKeyStore mKeyStore;
    private CancellationSignal mSignal;
    private OnAuthCallback mOnAuthCallback;

    FingerApi23Impl() {
        mKeyStore = new FingerKeyStore();
    }

    @RequiresApi(Build.VERSION_CODES.M)
    void authenticate(Context context) {
        try {
            // 创建需要的变量
            FingerprintManagerCompat.CryptoObject cryptoObject = providerCryptoObject();
            mSignal = new CancellationSignal();

            // 开始认证
            FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context);
            managerCompat.authenticate(cryptoObject, 0, mSignal, new CallbackAdapter(mOnAuthCallback) {
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
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
        Key secretKey = mKeyStore.getKey(KEY_NAME);

        // 生成Cipher
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        cipher.init(Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE, secretKey);

        return new FingerprintManagerCompat.CryptoObject(cipher);
    }

    /**
     * 创建本地 公私钥，系统自动保存到 AndroidKeyStore中
     */
    @RequiresApi(Build.VERSION_CODES.M)
    void createKeyInner() {
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
     * 从AndroidKeyStore中获取PublicKey，如果没有则代表本APP，没有开通指纹支付
     *
     * @return null if not create
     */
    PublicKey getPublicKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            PublicKey publicKey = keyStore.getCertificate(ECC_KEY_NAME).getPublicKey();
            if (null == publicKey) {
                return null;
            } else { // 由于需要传输给后台，因此使用X09过滤一遍
                KeyFactory factory = KeyFactory.getInstance(publicKey.getAlgorithm());
                X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
                return factory.generatePublic(spec); // verifyPublicKey
            }
        } catch (KeyStoreException e) {
            LogUtil.e("KeyStoreException", e);
        } catch (CertificateException e) {
            LogUtil.e("CertificateException", e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e("NoSuchAlgorithmException", e);
        } catch (IOException e) {
            LogUtil.e("IOException", e);
        } catch (InvalidKeySpecException e) {
            LogUtil.e("InvalidKeySpecException", e);
        }

        return null;
    }

    /**
     * 从AndroidKeyStore中获取PrivateKey，如果没有则代表本APP，没有开通指纹支付
     *
     * @return null if not create
     */
    PrivateKey getPrivateKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return (PrivateKey) keyStore.getKey(ECC_KEY_NAME, null);
        } catch (KeyStoreException e) {
            LogUtil.e("KeyStoreException", e);
        } catch (CertificateException e) {
            LogUtil.e("CertificateException", e);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e("NoSuchAlgorithmException", e);
        } catch (IOException e) {
            LogUtil.e("IOException", e);
        } catch (UnrecoverableKeyException e) {
            LogUtil.e("UnrecoverableKeyException", e);
        }

        return null;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    void authenticateEnroll(Context context) {
        mSignal = new CancellationSignal();

        FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context.getApplicationContext());
        managerCompat.authenticate(null, 0, mSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);

                createKeyInner();
                PublicKey publicKey = getPublicKey();
            }
        }, null);
    }

    /**
     * 验证并添加
     */
    @RequiresApi(Build.VERSION_CODES.M)
    void authenticateAdd(Context context, final String goodsInfo) {
        try {
            FingerprintManagerCompat.CryptoObject object = mKeyStore.getEncryptObject(KEY_NAME);
            mSignal = new CancellationSignal();

            FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context.getApplicationContext());
            managerCompat.authenticate(object, 0, mSignal, new CallbackAdapter(mOnAuthCallback) {
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    if (null != mSignal) {
                        mSignal.cancel();
                    }

                    Cipher cipher = result.getCryptoObject().getCipher();
                    if (null == cipher) {
                        super.onAuthenticationError(-1, "cipher is null");
                        return;
                    }

                    try {
                        byte[] encryptBytes = cipher.doFinal(goodsInfo.getBytes());
                        byte[] initVectorBytes = cipher.getIV();

                        String encryptStr = Base64.encodeToString(encryptBytes, Base64.NO_WRAP);
                        String initVectorStr = Base64.encodeToString(initVectorBytes, Base64.NO_WRAP);

                        LogUtil.v("encryptStr = " + encryptStr + ", initVectorStr = " + initVectorStr);
                        // .... ?????? 它的做法是保存起来

                        super.onAuthenticationSucceeded(result);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                        super.onAuthenticationError(-1, "BadPaddingException");
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                        super.onAuthenticationError(-1, "IllegalBlockSizeException");
                    }
                }
            }, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 验证并校验
     */
    @RequiresApi(Build.VERSION_CODES.M)
    void authenticateVerify(Context context, final String encryptData, String initVectorStr) {
        try {
            byte[] initVectorBytes = Base64.decode(initVectorStr, Base64.NO_WRAP);
            FingerprintManagerCompat.CryptoObject object = mKeyStore.getDecryptObject(KEY_NAME, initVectorBytes);
            mSignal = new CancellationSignal();

            FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context.getApplicationContext());
            managerCompat.authenticate(object, 0, mSignal, new CallbackAdapter(mOnAuthCallback) {
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    if (null != mSignal) {
                        mSignal.cancel();
                    }

                    Cipher cipher = result.getCryptoObject().getCipher();
                    if (null == cipher) {
                        super.onAuthenticationError(-1, "cipher is null");
                        return;
                    }

                    try {
                        byte[] encryptBytes = Base64.decode(encryptData, Base64.NO_WRAP);
                        byte[] sourceBytes = cipher.doFinal(encryptBytes);
                        String sourceData = new String(sourceBytes);
                        LogUtil.v("sourceData = " + sourceData);

                        // .... ?????? 它的做法是传出去，展示一下

                        super.onAuthenticationSucceeded(result);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                        super.onAuthenticationError(-1, "BadPaddingException");
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                        super.onAuthenticationError(-1, "IllegalBlockSizeException");
                    }
                }
            }, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 做一次适配
    private static class CallbackAdapter extends FingerprintManagerCompat.AuthenticationCallback {
        private OnAuthCallback sOnAuthCallback;

        private CallbackAdapter(OnAuthCallback callback) {
            this.sOnAuthCallback = callback;
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            if (null != sOnAuthCallback) {
                sOnAuthCallback.onAuthError(errMsgId, errString);
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            if (null != sOnAuthCallback) {
                sOnAuthCallback.onAuthFailed();
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            if (null != sOnAuthCallback) {
                sOnAuthCallback.onAuthHelp(helpMsgId, helpString);
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            if (null != sOnAuthCallback) {
                sOnAuthCallback.onAuthSucceeded();
            }
        }
    }
}
