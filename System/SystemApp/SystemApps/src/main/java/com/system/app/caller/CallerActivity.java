package com.system.app.caller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * 拨打电话
 *
 * @author linjiang@kjtpay.com  2019/4/17 -- 17:59
 */
public class CallerActivity extends BaseTestActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, CallerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("拨打", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtils.call(CallerActivity.this, "17682305090");
            }
        });
    }

    @Override
    protected String[] initRequestPermission() {
        return new String[]{Manifest.permission.CALL_PHONE};
    }
}
