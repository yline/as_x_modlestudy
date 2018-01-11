package com.view.wm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import com.view.wm.manager.FloatWindowManager;

/**
 * 悬浮框 服务
 *
 * @author yline 2018/1/11 -- 11:56
 * @version 1.0.0
 */
public class FloatService extends Service {
    private static View.OnClickListener mOnClickListener; // 非静态的，无法放入
    private static FloatService mFloatService;

    private FloatWindowManager mFloatWindowManager;

    public static void launcher(Context context) {
        Intent intent = new Intent(context, FloatService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initFloatWindowManager();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initFloatWindowManager() {
        if (null == mFloatWindowManager) {
            mFloatWindowManager = new FloatWindowManager(this);
            mFloatWindowManager.updateView();
            mFloatWindowManager.setOnFloatClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnClickListener) {
                        mOnClickListener.onClick(v);
                    }
                }
            });
        }
    }

    public static void registerCircleClickListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public static void unRegisterCircleClickListener() {
        mOnClickListener = null;
    }

    public static FloatService getFloatService() {
        return mFloatService;
    }
}
