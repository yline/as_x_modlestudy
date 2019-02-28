package com.yline.finger;

import android.os.Bundle;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.finger.manager.FingerManager;
import com.yline.finger.manager.OnAuthCallback;
import com.yline.finger.service.MockService;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

/**
 * 参考：
 * https://github.com/gaoyangcr7/BiometricPromptDemo // github 主要
 * https://blog.csdn.net/love_xiaozhao/article/details/81316145 // blog看的不多
 * https://github.com/PopFisher/FingerprintRecognition // github 第一个，写的很乱，后来换成另一个了
 * https://www.cnblogs.com/popfisher/p/6063835.html // blog 对应上个github
 * https://github.com/feelschaotic/FingerPrintAuth // github
 *
 * 1）其中CryptoObject crypto是可以为null，但是这个建议最好要实现一个加密的对象，有利于在使用到CancellationSignal 对象时候，用来取消当前已开启识别的进程。
 * 例如个人项目中就出现在一次已经开启了指纹识别中，用户需要关闭这个识别功能，但是为了安全只能是用户本人指纹验证过后来关闭这个功能，
 * 故此时又要开启另一个页面来验证指纹，这时候若没有加密的对象话，会导致第二次开启不了，因为crypto是和每次call关联，
 * 同时开启第二次手机会自动把第一次的关闭，执行第一次的cancel.cancel（）方法，导致第二次无故躺枪被认为了已经取消了，启动不了。
 *
 * 2）针对如上1）场景即使生成了crypto，还会有一个新问题（此时心里1万个草泥马奔腾），
 * 由于手机会自动把第一次的关闭，取消上次操作，倒好直接回调第一次的取消的监听，告诉你被取消了。
 * 但是还回调一次AuthenticationCallback.onAuthenticationError（）并且是在第二次调用中的新对象的回调里面，
 * 按道理如果这里不共用回调对象AuthenticationCallback，也是在第一个对象中回调啊。这个失败回调导致流程打乱了，
 * 其实这时候已经重新开启了，这不是把流程干扰了吗，纠结好久，用多种方式来判断，这个时候的回调不处理，总觉得不够完美，
 * 后想到了一个办法，在开启第二次时候，先把第一次的启动主动cancel掉，然后再启动第二次，当时觉得这个办法好，可是还是一样的情况，后来发现必须要间隔一段时间。
 *
 * 3）在上文中提到了需要设置锁屏，锁屏之后，可以通过指纹解锁啊，
 * 这个时候，如果应用中已经在等待用户输入指纹，再锁屏并解锁，发现返回应用之后，
 * 已经被取消了，原因同理，都是不能同时开启两次，会把上次的取消掉。
 *
 * https://www.programcreek.com/java-api-examples/index.php?api=android.security.keystore.KeyPermanentlyInvalidatedException
 * 指纹识别异常，针对新增情况处理
 *
 * https://blog.csdn.net/u011339364/article/details/78667701 // 感觉很重要
 * fingerId
 *
 * https://android-developers.googleblog.com/2015/10/new-in-android-samples-authenticating.html // 感觉可行的方案
 *
 * https://github.com/googlesamples/android-AsymmetricFingerprintDialog // google对应的提供方案
 *
 *
 * @author yline 2019/2/25 -- 18:02
 */
public class MainActivity extends BaseTestActivity {

    private OnAuthCallback authCallback = new OnAuthCallback() {
        @Override
        public void onAuthError(int errorCode, CharSequence errString) {
            super.onAuthError(errorCode, errString);
            SDKManager.toast("校验Error");
            LogUtil.v("errorCode = " + errorCode + ", errString = " + errString);
        }

        @Override
        public void onAuthFailed() {
            super.onAuthFailed();
            SDKManager.toast("校验Failed");
            LogUtil.v("");
        }

        @Override
        public void onAuthHelp(int helpCode, CharSequence helpString) {
            super.onAuthHelp(helpCode, helpString);
            SDKManager.toast("校验help");
            LogUtil.v("helpCode = " + helpCode + ", helpString = " + helpString);
        }

        @Override
        public void onAuthSucceeded() {
            super.onAuthSucceeded();
            LogUtil.v("");
            SDKManager.toast("验证成功");
        }
    };

    private MockService.SingleValue mSingleValue;

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

        addButton("指纹校验开始, 23", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid()) {
                    SDKManager.toast("指纹识别不支持或未开启");
                    return;
                }

                SDKManager.toast("等待校验");
                FingerManager.authenticate23(MainActivity.this, authCallback);
            }
        });

        addButton("23，指纹加密", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceData = "123456789";
                FingerManager.authenticate23Add(MainActivity.this, sourceData, authCallback);
            }
        });

        addButton("23，指纹解密，获取参数", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSingleValue = MockService.getSingleValue();

                String encryptStr = mSingleValue.getEncryptStr();
                String initVectorStr = mSingleValue.getInitVector();

                LogUtil.v("encryptStr = " + encryptStr + "initVectorStr = " + initVectorStr);
            }
        });

        addButton("23，指纹解密，实现", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mSingleValue) {
                    return;
                }

                LogUtil.v("开始解密");
                String encryptStr = mSingleValue.getEncryptStr();
                String initVectorStr = mSingleValue.getInitVector();

                FingerManager.authenticate23Verify(MainActivity.this, encryptStr, initVectorStr, authCallback);
            }
        });

        addButton("指纹校验开始, 28", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid()) {
                    SDKManager.toast("指纹识别不支持或未开启");
                    return;
                }

                SDKManager.toast("等待校验");
                FingerManager.authenticate28(MainActivity.this, authCallback);
            }
        });
    }

    private boolean checkValid() {
        boolean isEnable = FingerManager.isFingerEnable(MainActivity.this);
        if (!isEnable) {
            FingerManager.openFingerSetting(MainActivity.this);
        }
        return isEnable;
    }
}
