package com.yline.process.alive.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

/**
 * 进程保活【挺流氓的】
 * 鉴于当app被杀死后是监听不到系统广播的，而我们还需要保持DaemonService以确保推送TCP连接的建立，
 * 那我们可以在DaemonService的onDestroy()中启动一个新的service DaemonReStartService,
 * 在DaemonReStartService中来重新启动DaemonService。
 *
 * @author yline 2019/7/1 -- 16:51
 */
public class DaemonService extends Service {
    private static final int DAEMON_SERVICE_ID = 321654987;
    private static boolean mAlive = false;

    public static void launcher(Context context) {
        if (mAlive) {
            return;
        }

        Intent serviceIntent = new Intent(context, DaemonService.class);
        context.startService(serviceIntent);
    }

    public static boolean isAlive() {
        return mAlive;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAlive = true;
    }

    @Override
    public void onDestroy() {
        mAlive = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // API < 18时，直接传入new Notification()
            startForeground(DAEMON_SERVICE_ID, new Notification());
        } else {
            // API >= 18时，启动两个id相同的service，然后将后startForeground的service stopForeground/stop
            startService(new Intent(this, DaemonInnerService.class));
            startForeground(DAEMON_SERVICE_ID, new Notification());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 用于API >= 18时灰色保活Service
     */
    public static class DaemonInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(DAEMON_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
