package com.process.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yline.base.BaseActivity;

/**
 * Push 进程
 *
 * @author yline 2018/4/19 -- 16:10
 * @version 1.0.0
 */
public class PushProcessActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, PushProcessActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
