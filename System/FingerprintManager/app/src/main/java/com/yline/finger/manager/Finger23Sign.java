package com.yline.finger.manager;

import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Base64;

import com.yline.finger.helper.SignUtil;
import com.yline.utils.LogUtil;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class Finger23Sign {
    private static final int STATE_OPEN = 1;
    private static final int STATE_REOPEN = 2;
    private static final int STATE_SUCCESS = 3;

    /**
     * 关闭指纹支付，并删除公私钥
     *
     * @return true(本地公私钥 ， 清除成功)
     */
    public static boolean delete() {
        LogUtil.v("delete");
        return SignUtil.deleteKey();
    }

    /**
     * 指纹未开通、秘钥未创建情况【失败情况，暂时不处理】
     */
    public static void open(Context context, final OnOpenCallback callback) {
        LogUtil.v("open");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("open failed, version is " + Build.VERSION.SDK_INT + ", smaller than 23");
            return;
        }

        final KeyStore keyStore = SignUtil.getKeyStore();
        if (null == keyStore) {
            LogUtil.e("open failed, keystore is null");
            return;
        }

        if (SignUtil.isKeyExist(keyStore)) {
            LogUtil.e("open failed, key is existed");
            return;
        }

        // 首次创建，秘钥肯定不会失效
        SignUtil.createKey();

        // 加入签名，准备指纹验证
        try {
            PrivateKey privateKey = SignUtil.getPrivateKey(keyStore);
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);

            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
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

                    // 创建成功，将公钥传出
                    if (null != callback) {
                        PublicKey publicKey = SignUtil.getPublicKey(keyStore);
                        callback.onOpen(publicKey);
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        LogUtil.e("open exception");
    }

    public interface OnOpenCallback {
        /**
         * 开通指纹，创建秘钥
         *
         * @param publicKey 公钥，允许上传服务器
         */
        void onOpen(PublicKey publicKey);
    }

    /**
     * 指纹未开通，秘钥不存在；则创建秘钥，验证指纹，并要求输入支付密码
     * 指纹已开通，秘钥失效；则验证指纹，并要求输入支付密码
     * 指纹已开通，秘钥有效；直接通过
     */
    public static void verify(Context context, final String goodsInfo, final OnVerifyCallback callback) {
        LogUtil.v("verify");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("verify failed, version is " + Build.VERSION.SDK_INT + ", smaller than 23");
            return;
        }

        final KeyStore keyStore = SignUtil.getKeyStore();
        if (null == keyStore) {
            LogUtil.e("keystore is null");
            return;
        }

        int state = 0; // 初始状态
        FingerprintManagerCompat.CryptoObject cryptoObject = null;
        if (!SignUtil.isKeyExist(keyStore)) { // 秘钥不存在
            LogUtil.v("verify, key is not exist");

            // 首次创建，秘钥肯定不会失效
            SignUtil.createKey();
            Signature signature = providerSignature(keyStore);
            if (null == signature) {
                LogUtil.e("verify failed, first create signature failed");
            } else {
                cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
                state = STATE_OPEN; // 秘钥不存在时，创建成功
            }
        } else {
            Signature signature = providerSignature(keyStore);
            if (null == signature) { // 秘钥存在，但是秘钥失效
                SignUtil.deleteKey(keyStore); // 删除秘钥
                SignUtil.createKey(); // 重新创建秘钥
                signature = providerSignature(keyStore); // 获取秘钥
                if (null == signature) {
                    LogUtil.e("verify failed, create signature failed");
                } else {
                    cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
                    state = STATE_REOPEN; // 秘钥存在，但失效；创建秘钥成功
                }
            } else { // 秘钥存在，并有效
                cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);
                state = STATE_SUCCESS; // 秘钥存在，并有效
            }
        }

        if (null == cryptoObject) {
            LogUtil.e("verify failed cryptoObject is null");
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

                if (finalState == STATE_OPEN) { // 秘钥不存在，创建
                    if (null != callback) {
                        PublicKey publicKey = SignUtil.getPublicKey(keyStore);
                        callback.onOpen(publicKey);
                    }
                } else if (finalState == STATE_REOPEN) { // 秘钥存在，但是失效，重新创建
                    if (null != callback) {
                        PublicKey publicKey = SignUtil.getPublicKey(keyStore);
                        callback.onReopen(publicKey);
                    }
                } else if (finalState == STATE_SUCCESS) { // 秘钥存在，并有效；对商品生成签名
                    try {
                        Signature signature = result.getCryptoObject().getSignature();
                        if (null == signature) {
                            LogUtil.e("verify failed, signature is null");
                            return;
                        }

                        signature.update(goodsInfo.getBytes());
                        byte[] signBytes = signature.sign();
                        String signValue = Base64.encodeToString(signBytes, Base64.NO_WRAP);
                        LogUtil.v("verify, signValue = " + signValue);
                        if (null != callback) {
                            PublicKey publicKey = SignUtil.getPublicKey(keyStore);
                            callback.onSuccess(publicKey, signValue);
                        }
                    } catch (SignatureException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface OnVerifyCallback {
        /**
         * 秘钥不存在，创建
         *
         * @param publicKey 公钥
         */
        void onOpen(PublicKey publicKey);

        /**
         * 秘钥存在，但是失效，重新创建
         *
         * @param publicKey 公钥
         */
        void onReopen(PublicKey publicKey);

        /**
         * 秘钥存在，并有效；对商品生成签名
         *
         * @param publicKey 本来不需要的，但是MockService无法常量储存，导致重开APP，就再也无法校验了
         * @param signValue 签名信息
         */
        void onSuccess(PublicKey publicKey, String signValue);
    }

    /**
     * 获取加密 Signature
     * 如果返回null，则当做秘钥失效处理
     *
     * @return null if 秘钥失效 或 其他异常
     */
    private static Signature providerSignature(KeyStore keyStore) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            PrivateKey privateKey = SignUtil.getPrivateKey(keyStore);
            signature.initSign(privateKey);

            return signature;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtil.e("providerCryptoObject exception");
        return null;
    }
}
