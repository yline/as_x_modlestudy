package com.yline.finger;

import android.os.Bundle;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.finger.manager.FingerManager;
import com.yline.finger.manager.OnAuthCallback;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

/**
 * 参考：
 * https://github.com/gaoyangcr7/BiometricPromptDemo // github 主要
 * https://blog.csdn.net/love_xiaozhao/article/details/81316145 // blog看的不多
 * https://github.com/PopFisher/FingerprintRecognition // github 第一个，写的很乱，后来换成另一个了
 * https://www.cnblogs.com/popfisher/p/6063835.html // blog 对应上个github
 *
 * @author yline 2019/2/25 -- 18:02
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("指纹校验开始, 23", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid()) {
                    SDKManager.toast("指纹识别不支持或未开启");
                    return;
                }

                FingerManager.authenticate23(MainActivity.this, new OnAuthCallback() {
                    @Override
                    public void onAuthError(int errorCode, CharSequence errString) {
                        super.onAuthError(errorCode, errString);
                        LogUtil.v("errorCode = " + errorCode + ", errString = " + errString);
                    }

                    @Override
                    public void onAuthFailed() {
                        super.onAuthFailed();
                        LogUtil.v("");
                    }

                    @Override
                    public void onAuthHelp(int helpCode, CharSequence helpString) {
                        super.onAuthHelp(helpCode, helpString);
                        LogUtil.v("helpCode = " + helpCode + ", helpString = " + helpString);
                    }

                    @Override
                    public void onAuthSucceeded() {
                        super.onAuthSucceeded();
                        LogUtil.v("");
                        SDKManager.toast("验证成功");
                    }
                });
            }
        });

        addButton("指纹校验开始, 28", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid()) {
                    SDKManager.toast("指纹识别不支持或未开启");
                    return;
                }

                FingerManager.authenticate28(MainActivity.this, new OnAuthCallback() {
                    @Override
                    public void onAuthError(int errorCode, CharSequence errString) {
                        super.onAuthError(errorCode, errString);
                        LogUtil.v("errorCode = " + errorCode + ", errString = " + errString);
                    }

                    @Override
                    public void onAuthFailed() {
                        super.onAuthFailed();
                        LogUtil.v("");
                    }

                    @Override
                    public void onAuthHelp(int helpCode, CharSequence helpString) {
                        super.onAuthHelp(helpCode, helpString);
                        LogUtil.v("helpCode = " + helpCode + ", helpString = " + helpString);
                    }

                    @Override
                    public void onAuthSucceeded() {
                        super.onAuthSucceeded();
                        LogUtil.v("");
                        SDKManager.toast("验证成功");
                    }
                });
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
