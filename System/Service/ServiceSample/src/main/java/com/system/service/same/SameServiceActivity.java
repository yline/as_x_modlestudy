package com.system.service.same;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

/**
 * Created by yline on 2016/11/9.
 */
public class SameServiceActivity extends BaseTestActivity implements ServiceConnection {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, SameServiceActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private static final int BIND_SERVICE_CODE = 0;

    private SameService.TestBinder sBinder;

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        LogUtil.v("onCreate pid = " + Process.myPid() + ", tid = " + Process.myTid());

        // 开启服务
        addButton("开启服务", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SameService.serviceStart(SameServiceActivity.this);
            }
        });

        // conn执行
        addButton("conn", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("conn sBinder = " + sBinder);
                if (null != sBinder) {
                    sBinder.execute();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        SameService.serviceBind(this, this, BIND_SERVICE_CODE);
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
        LogUtil.v("onServiceConnected pid = " + Process.myPid() + ", tid = " + Process.myTid());
        sBinder = (SameService.TestBinder) service;
    }

    /**
     * 连接意外丢失时调用;
     * 而解除绑定或service崩溃或被强杀都不会调用
     *
     * @param name
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogUtil.v("onServiceDisconnected pid = " + Process.myPid() + ", tid = " + Process.myTid());
    }

    @Override
    protected void onStop() {
        super.onStop();
        SameService.serviceUnbind(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SameService.serviceStop(this);
    }

}
