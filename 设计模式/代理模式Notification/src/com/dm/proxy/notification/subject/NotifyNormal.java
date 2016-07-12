package com.dm.proxy.notification.subject;

import com.dm.proxy.notification.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;

public class NotifyNormal extends Notify
{
    
    public NotifyNormal(Context context)
    {
        super(context);
    }
    
    @SuppressLint("NewApi")
    @Override
    public void send()
    {
        Notification n = builder.build();
        
        n.contentView = new RemoteViews(context.getPackageName(), R.layout.remote_notify_proxy_normal);
        
        nm.notify(0, n);
    }
    
    @Override
    public void cancel()
    {
        nm.cancel(0);
    }
    
}
