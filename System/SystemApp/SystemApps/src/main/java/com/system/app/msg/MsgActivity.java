package com.system.app.msg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * 短信 todo 测试
 *
 * @author yline 2019/4/17 -- 18:12
 */
public class MsgActivity extends BaseTestActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, MsgActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        /*new MsgHelper().registerMsgobserver(MainApplication.getApplication());
        findViewById(R.id.btn_msg_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MsgHelper().sendMsg();
            }
        });

                findViewById(R.id.btn_msg_read).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MsgHelper().readMsgAll(MainActivity.this);
            }
        });
        */
    }
}
