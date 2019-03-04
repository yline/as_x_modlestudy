package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Base64;

import com.yline.utils.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Finger23Impl {
    private static final String ECC_KEY_NAME = "Yline_Finger_Pay";

    private CancellationSignal mSignal;

    /**
     * 创建公钥、私钥对；然后本地留有私钥，用作签名；后台留有公钥，进行验证签名
     *
     * @param context  上下文
     * @param callback 回调
     */
    @RequiresApi(Build.VERSION_CODES.M)
    void authenticateEnroll(Context context, final OnEnrollCallback callback) {
        mSignal = new CancellationSignal();

        LogUtil.v("");
        // 获取本地，是否已存在公钥；若已存在，则关闭
        PublicKey publicKey = getPublicKey();
        if (null != publicKey) {
            LogUtil.e("该指纹已经开通过！！！");
            return;
        }

        // 首次，则创建[待测试，和卸载是否有关联]
        createKeyInner();
        final PublicKey finalPublicKey = getPublicKey();
        if (null == finalPublicKey) {
            LogUtil.e("publicKey create Failed");
            return;
        }

        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            PrivateKey privateKey = getPrivateKey();
            if (null == privateKey) {
                LogUtil.e("privateKey never created");
                return;
            }
            signature.initSign(privateKey);
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);

            new FingerCompat().authenticate(context, cryptoObject, mSignal, new FingerprintManagerCompat.AuthenticationCallback() {
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

                    // 创建成功，将公钥传出
                    if (null != callback) {
                        callback.onEnroll(Finger23Impl.this, finalPublicKey);
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e("NoSuchAlgorithmException", e);
        } catch (InvalidKeyException e) {
            LogUtil.e("InvalidKeyException", e);
        }
    }

    /**
     * 录入指纹，并给商品上一个签名
     *
     * @param goodsInfo 商品信息
     */
    @RequiresApi(Build.VERSION_CODES.M)
    void authenticateVerify(Context context, final String goodsInfo, final OnVerifyCallback callback) {
        try {
            mSignal = new CancellationSignal();

            Signature signature = Signature.getInstance("SHA256withECDSA");
            PrivateKey privateKey = getPrivateKey();
            if (null == privateKey) {
                LogUtil.e("privateKey never created");
                return;
            }
            signature.initSign(privateKey);
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
            new FingerCompat().authenticate(context, cryptoObject, mSignal, new FingerprintManagerCompat.AuthenticationCallback() {
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

                    Signature signature = result.getCryptoObject().getSignature();
                    if (null == signature) {
                        LogUtil.e("signature is null");
                        return;
                    }

                    try {
                        signature.update(goodsInfo.getBytes());
                        byte[] signBytes = signature.sign();
                        String signValue = Base64.encodeToString(signBytes, Base64.NO_WRAP);
                        LogUtil.v("signValue = " + signValue);
                        if (null != callback) {
                            callback.onVerify(Finger23Impl.this, signValue);
                        }
                    } catch (SignatureException e) {
                        LogUtil.e("SignatureException", e);
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e("NoSuchAlgorithmException", e);
        } catch (InvalidKeyException e) {
            LogUtil.e("InvalidKeyException", e);
        }
    }

    public void cancel() {
        if (null != mSignal) {
            mSignal.cancel();
        }
    }

    public interface OnVerifyCallback {
        void onVerify(Finger23Impl finger23, String signValue);
    }

    public interface OnEnrollCallback {
        void onEnroll(Finger23Impl finger23, @NonNull PublicKey publicKey);
    }

    /**
     * 创建本地 公私钥，系统自动保存到 AndroidKeyStore中
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private void createKeyInner() {
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
    private PublicKey getPublicKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

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
    private PrivateKey getPrivateKey() {
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
}
