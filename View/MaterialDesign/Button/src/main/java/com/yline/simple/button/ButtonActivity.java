package com.yline.simple.button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseActivity;
import com.yline.simple.R;

/**
 * 需要配置
 * drawable 目录
 * drawable-v21 目录
 *
 * @author yline 2020/5/18 -- 14:01
 */
public class ButtonActivity extends BaseActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, ButtonActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
    }
}
