package com.system.service.diff;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

public class DiffServiceActivity extends BaseTestActivity implements ServiceConnection {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, DiffServiceActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }


    private static final int BIND_SERVICE_CODE = 0;
    private Messenger mMessenger;
    private Messenger mReplyMessenger;

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        LogUtil.v("testStart");

        // 开启服务
        addButton("开启服务", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiffService.serviceStart(DiffServiceActivity.this);
            }
        });

        // conn执行
        addButton("conn", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("conn mMessenger = " + mMessenger);
                if (null != mMessenger) {
                    initReplyMessenger();

                    try {
                        Message message = Message.obtain(null, 5);
                        message.replyTo = mReplyMessenger;

                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        LogUtil.e("", e);
                    }
                }
            }
        });
    }

    private void initReplyMessenger() {
        if (null == mReplyMessenger) {
            mReplyMessenger = new Messenger(new ReplyHandler());
        }
    }

    // 这是调用后，回复的数据
    private static class ReplyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.v("reply msg = " + msg);
        }
    }

    @Override
    protected void onStart() {
        DiffService.serviceBind(this, this, BIND_SERVICE_CODE);
        super.onStart();
    }

    /**
     * 成功连接时,传入Service
     *
     * @param name
     * @param service
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LogUtil.v("onServiceConnected");
        mMessenger = new Messenger(service); // 获取Service的Messenger
    }

    /**
     * 连接意外丢失时调用;
     * 而解除绑定或service崩溃或被强杀都不会调用
     *
     * @param name
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogUtil.v("onServiceDisconnected");
    }

    @Override
    protected void onStop() {
        super.onStop();
        DiffService.serviceUnbind(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DiffService.serviceStop(this);
    }
}
