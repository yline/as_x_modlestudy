package com.system.app.msg;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.util.concurrent.Executors;

/**
 * 权限：android.permission.READ_SMS
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
    protected String[] initRequestPermission() {
        return new String[]{Manifest.permission.READ_SMS};
    }

    private MessageContentObserver mContentObserver;
    private MessageReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessageReceiver = MessageReceiver.register(this);
        mContentObserver = MessageContentObserver.register(this);

        // 广播收到的内容
        mMessageReceiver.setOnMessageCallback(new MessageReceiver.OnMessageCallback() {
            @Override
            public void onReceive(String msg) {
                LogUtil.v("receiver msg = " + msg);
            }
        });

        // 监听器收到的内容
        mContentObserver.setOnMessageCallback(new MessageContentObserver.OnMessageCallback() {
            @Override
            public void onObserver(String msg) {
                LogUtil.v("observer msg = " + msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MessageContentObserver.unregister(mContentObserver);
        MessageReceiver.unregister(this, mMessageReceiver);
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        final EditText phoneEdit = addEditNumber("", "17682305090");
        final EditText contentEdit = addEditNumber("", "验证码为：556254，请在5分钟内输入。您正在登陆快捷通账户。为了您的账户安全，请勿将验证码告诉他人。如非本人操作，请致电4006110909。");
        addButton("发送短信", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEdit.getText().toString().trim();
                String content = contentEdit.getText().toString().trim();

                LogUtil.v("phone = " + phone + ", content = " + content);
                MsgUtils.sendMessage(phone, content);
            }
        });

        addButton("读取所有短信", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        MsgUtils.readMessage(MsgActivity.this);
                    }
                });
            }
        });

        final EditText codeEdit = addEditNumber("", "【快捷通支付】验证码为：556255，请在5分钟内输入。您正在登陆快捷通账户。为了您的账户安全，请勿将验证码告诉他人。如非本人操作，请致电4006110909。");
        addButton("解析短信验证码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeMessage = codeEdit.getText().toString().trim();
                String code = MsgUtils.parseCodeFromMessage(codeMessage);
                LogUtil.v("code");
            }
        });
    }
}
