package com.system.service.diff;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yline.utils.LogUtil;

import java.lang.ref.WeakReference;

public class DiffService extends Service {
    public static void serviceStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DiffService.class);
        context.startService(intent);
    }

    public static void serviceStop(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DiffService.class);
        context.stopService(intent);
    }

    public static void serviceBind(Context context, @NonNull ServiceConnection conn, int flags) {
        Intent intent = new Intent();
        intent.setClass(context, DiffService.class);
        context.bindService(intent, conn, flags);
    }

    public static void serviceUnbind(Context context, @NonNull ServiceConnection conn) {
        context.unbindService(conn);
        conn = null;
    }

    private static class DiffServiceHandler extends Handler {
        private final WeakReference<DiffService> mServiceReference;

        public DiffServiceHandler(DiffService service) {
            super();
            mServiceReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            DiffService service = mServiceReference.get();
            if (null == service) {
                LogUtil.v("service handle msg = " + msg);
                return;
            }

            try {
                LogUtil.v("service handler msg = " + msg);

                Messenger replyMessenger = msg.replyTo; // 获取用于回复的Messenger
                Message newMessage = Message.obtain(null, 10);
                if (null != replyMessenger) {
                    replyMessenger.send(newMessage);
                }
            } catch (RemoteException e) {
                LogUtil.e("reply error", e);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.v("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int superReturn = super.onStartCommand(intent, flags, startId);
        LogUtil.v("onStartCommand superResult = " + superReturn + "，");
        return superReturn;
    }

    private Messenger mMessenger = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.v("onBind");

        if (null == mMessenger) {
            mMessenger = new Messenger(new DiffServiceHandler(this));
        }
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean superReturn = super.onUnbind(intent);
        LogUtil.v("onUnbind superResult = " + superReturn + "，");
        return superReturn;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v("onDestroy");
    }
}
