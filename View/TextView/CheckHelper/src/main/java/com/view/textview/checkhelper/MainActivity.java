package com.view.textview.checkhelper;

import android.os.Bundle;
import android.view.View;

import com.view.textview.checkhelper.activity.PhoneICodeActivity;
import com.view.textview.checkhelper.activity.PhonePwdActivity;
import com.view.textview.checkhelper.activity.PhonePwdCodeActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("手机号+密码校验", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhonePwdActivity.launcher(MainActivity.this);
            }
        });

        addButton("手机号+倒计时验证码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneICodeActivity.launcher(MainActivity.this);
            }
        });

        addButton("手机号+密码+倒计时验证码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhonePwdCodeActivity.launcher(MainActivity.this);
            }
        });
    }
}
