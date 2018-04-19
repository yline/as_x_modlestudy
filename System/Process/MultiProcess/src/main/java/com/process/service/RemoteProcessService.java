package com.process.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author yline 2018/4/19 -- 17:04
 * @version 1.0.0
 */
public class RemoteProcessService extends Service {
    public static void launcher(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RemoteProcessService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
