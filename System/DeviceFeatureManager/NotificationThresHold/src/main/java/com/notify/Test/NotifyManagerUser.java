package com.notify.Test;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.notify.NotifyManager;
import com.notify.R;
import com.notify.activity.MainActivity;

/**
 * Created by yline on 2016/10/29.
 */
public class NotifyManagerUser
{
	public void testBuild(Context context)
	{
		NotifyManager.getInstance().build()
				.setContentView(context, R.layout.activity_main)
				.show();
	}

	public void testBuildIn(Context context)
	{
		PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
		Notification.Builder build = new Notification.Builder(context)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setTicker("TickerText:" + "您有新短消息，请注意查收！")
				.setContentTitle("Notification Title")
				.setContentText("This is the notification message")
				.setContentIntent(pendingIntent3)
				.setNumber(1);
		NotifyManager.getInstance().buildIn().setBuilder(build).setFlags(Notification.FLAG_AUTO_CANCEL).show();
	}

	public void testCancel()
	{
		NotifyManager.getInstance().cancleAll();
	}
}
