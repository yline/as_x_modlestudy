package com.dm.proxy.notification.subject;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.design.pattern.proxy.notification.R;

public abstract class Notify {
    protected Context context;

    protected NotificationManager nm;

    protected NotificationCompat.Builder builder;

    public Notify(Context context) {
        this.context = context;

        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent();
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, flag);
        builder.setContentIntent(pendingIntent);
    }

    public abstract void send();

    public abstract void cancel();
}
