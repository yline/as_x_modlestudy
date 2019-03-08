package com.yline.finger;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.finger.manager.Finger23Crypt;
import com.yline.finger.manager.Finger23Sign;
import com.yline.finger.manager.FingerManager;
import com.yline.finger.service.HttpUtils;
import com.yline.finger.service.OnJsonCallback;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.security.PublicKey;

/**
 * @author yline 2019/2/25 -- 18:02
 */
public class MainActivity extends BaseTestActivity {

    @Override
    protected String[] initRequestPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("硬件和软件校验", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fingerEnable = FingerManager.isFingerEnable(MainActivity.this);
                if (fingerEnable) {
                    SDKManager.toast("硬件和软件支持");
                } else {
                    SDKManager.toast("硬件和软件不支持");
                    FingerManager.openFingerSetting(MainActivity.this);
                }
            }
        });

        addButton("安全性校验", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean secureByHardware = FingerManager.isSecureByHardware();
                if (secureByHardware) {
                    LogUtil.v("安全性校验通过");
                    SDKManager.toast("安全性校验通过");
                } else {
                    LogUtil.e("不安全");
                    SDKManager.toast("不安全");
                }
            }
        });

        addButton("23，指纹校验，无加密、无签名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerManager.auth23Simple(MainActivity.this);
            }
        });

        addButton("23，指纹校验，加密、可取消、无签名", new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String goodsInfo = System.currentTimeMillis() + "0yline";
                FingerManager.auth23WithEncrypt(MainActivity.this, goodsInfo, new Finger23Crypt.OnEncryptCallback() {
                    @Override
                    public void onEncrypt(final Finger23Crypt crypt) {
                        LogUtil.v("加密成功");
                        SDKManager.toast("加密成功");
                        crypt.cancel();
                    }
                });
            }
        });

        addButton("23，指纹校验，解密、可取消、无签名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerManager.auth23WithDecrypt(MainActivity.this, new Finger23Crypt.OnDecryptCallback() {
                    @Override
                    public void onDecrypt(Finger23Crypt crypt, String goodsInfo) {
                        LogUtil.v("解密成功， goodsInfo = " + goodsInfo);
                        SDKManager.toast("解密成功");
                        crypt.cancel();
                    }
                });
            }
        });

        addButton("23，指纹录入，签名创建、可取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerManager.auth23WithSignCreate(MainActivity.this, new Finger23Sign.OnOpenCallback() {
                    @Override
                    public void onOpen(PublicKey publicKey) {
                        HttpUtils.enroll(publicKey, new OnJsonCallback<String>() {
                            @Override
                            public void onResponse(String s) {
                            }
                        });
                    }
                });
            }
        });

        addButton("23，指纹校验，签名验证、可取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String goodsInfo = System.currentTimeMillis() + "-yline"; // 商品
                FingerManager.auth23WithSignVerify(MainActivity.this, goodsInfo, new Finger23Sign.OnVerifyCallback() {
                    @Override
                    public void onOpen(PublicKey publicKey) {
                        HttpUtils.enroll(publicKey, new OnJsonCallback<String>() {
                            @Override
                            public void onResponse(String s) {

                            }
                        });
                    }

                    @Override
                    public void onReopen(PublicKey publicKey) {
                        HttpUtils.enrollAndVerify(publicKey, new OnJsonCallback<String>() {
                            @Override
                            public void onResponse(String s) {

                            }
                        });
                    }

                    @Override
                    public void onSuccess(String signValue) {
                        HttpUtils.verifyByFinger(goodsInfo, signValue, new OnJsonCallback<String>() {
                            @Override
                            public void onResponse(String s) {
                            }
                        });
                    }
                });
            }
        });

        // 指纹购买，直接成功的
        addButton("密码校验", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils.verifyByPwd(null);
            }
        });

        addButton("28，指纹校验，无加密、无签名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerManager.auth28Simple(MainActivity.this);
            }
        });
    }
}
