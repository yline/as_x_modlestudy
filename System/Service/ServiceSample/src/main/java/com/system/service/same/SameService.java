package com.system.service.same;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

/**
 * 该示例包含:
 * 1,生命周期(onCreate、onStartCommand、onDestroy)
 * 2,binder流程(onBind、onUnbind);可以通过Binder通信(ServiceConnection)
 * onCreate首次开启的时候调用
 * onStartCommand每次开启的时候,都会调用一次
 *
 * @author yline 2016/11/10 --> 1:41
 * @version 1.0.0
 */
public class SameService extends Service {

    public static void serviceStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SameService.class);
        context.startService(intent);
    }

    public static void serviceStop(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SameService.class);
        context.stopService(intent);
    }

    public static void serviceBind(Context context, @NonNull ServiceConnection conn, int flags) {
        Intent intent = new Intent();
        intent.setClass(context, SameService.class);
        context.bindService(intent, conn, flags);
    }

    public static void serviceUnbind(Context context, @NonNull ServiceConnection conn) {
        context.unbindService(conn);
        conn = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.v("onCreate pid = " + Process.myPid() + ", tid = " + Process.myTid());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int superReturn = super.onStartCommand(intent, flags, startId);
        LogUtil.v("onStartCommand superResult = " + superReturn + "， pid = " + Process.myPid() + ", tid = " + Process.myTid());
        return superReturn;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.v("onBind pid = " + Process.myPid() + ", tid = " + Process.myTid());
        return new TestBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean superReturn = super.onUnbind(intent);
        LogUtil.v("onUnbind superResult = " + superReturn + "， pid = " + Process.myPid() + ", tid = " + Process.myTid());
        return superReturn;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v("onDestroy pid = " + Process.myPid() + ", tid = " + Process.myTid());
    }


    public class TestBinder extends Binder implements ITestBinderCallback {

        @Override
        public void execute() {
            LogUtil.v("TestBinder execute pid = " + Process.myPid() + ", tid = " + Process.myTid());
        }
    }

    public interface ITestBinderCallback {
        void execute();
    }
}
