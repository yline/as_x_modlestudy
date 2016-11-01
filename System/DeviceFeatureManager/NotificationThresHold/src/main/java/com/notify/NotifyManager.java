package com.notify;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

/**
 * 1,要求: 在Application中调用一次:setApplicationContext();
 * 使用方式1:
 * NotificationUtil.getInstance().build().setContentView(this, R.layout.my_notification).show();
 * 使用方式2:
 * PendingIntent pendingIntent3 =
 * PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
 * NotificationUtil.getInstance().buildIn()
 * .setBuilder(new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher)
 * .setTicker("TickerText:" + "您有新短消息，请注意查收！")
 * .setContentTitle("Notification Title")
 * .setContentText("This is the notification message")
 * .setContentIntent(pendingIntent3)
 * .setNumber(1))
 * .setFlags(Notification.FLAG_AUTO_CANCEL)
 * .show();
 * @author YLine
 *         2016年10月29日 下午3:33:53
 */
public class NotifyManager
{
	/** 系统服务管理类 */
	private static NotificationManager notificationManager;

	public static final int REQUEST_CODE = 0;

	private NotifyManager()
	{
	}

	private static NotifyManager instance;

	public static NotifyManager getInstance()
	{
		if (null == instance)
		{
			synchronized (NotifyManager.class)
			{
				if (null == instance)
				{
					instance = new NotifyManager();
				}
			}
		}
		return instance;
	}

	public static void setApplicationContext(Context context)
	{
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public NotificationBuilder build()
	{
		return new NotificationBuilder();
	}

	public class NotificationBuilder
	{
		/** 内容 */
		private String tickerText = "";

		/** 时间 */
		private long when = System.currentTimeMillis();

		private PendingIntent contentIntent;

		private int flags = Notification.FLAG_AUTO_CANCEL;

		private RemoteViews contentView;

		private int notifyId = 0;

		private int icon = R.mipmap.ic_launcher;

		public NotificationBuilder setTickerText(String tickerText)
		{
			this.tickerText = tickerText;
			return this;
		}

		public NotificationBuilder setWhen(long when)
		{
			this.when = when;
			return this;
		}

		public NotificationBuilder setContentIntent(PendingIntent contentIntent)
		{
			this.contentIntent = contentIntent;
			return this;
		}

		public NotificationBuilder setContentIntent(Context context, Class<?> cls)
		{
			Intent intent = new Intent();
			intent.setClass(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			this.contentIntent = pendingIntent;
			return this;
		}

		public NotificationBuilder setContentIntent(Context context, Class<?> cls, int styleFlag)
		{
			Intent intent = new Intent();
			intent.setClass(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, styleFlag);
			this.contentIntent = pendingIntent;
			return this;
		}

		public NotificationBuilder setFlags(int flags)
		{
			this.flags = flags;
			return this;
		}

		public NotificationBuilder setContentView(Context context, int layoutId)
		{
			this.contentView = new RemoteViews(context.getPackageName(), layoutId);
			return this;
		}

		public NotificationBuilder setContentView(RemoteViews contentView)
		{
			this.contentView = contentView;
			return this;
		}

		public NotificationBuilder setNotifyId(int notifyId)
		{
			this.notifyId = notifyId;
			return this;
		}

		public NotificationBuilder setIcon(int iconId)
		{
			this.icon = iconId;
			return this;
		}

		public void show()
		{
			Notification notification = new Notification();
			notification.tickerText = tickerText;
			notification.when = when;
			notification.icon = icon;
			notification.contentIntent = contentIntent;
			notification.flags = flags;
			notification.contentView = contentView;

			notificationManager.notify(notifyId, notification);
		}
	}

	public NotificationBuilderIn buildIn()
	{
		return new NotificationBuilderIn();
	}

	public class NotificationBuilderIn
	{
		private int notifyId = 0;

		private Notification.Builder builder;

		private int flags = Notification.FLAG_AUTO_CANCEL;

		public NotificationBuilderIn setNotifyId(int notifyId)
		{
			this.notifyId = notifyId;
			return this;
		}

		public NotificationBuilderIn setFlags(int flags)
		{
			this.flags = flags;
			return this;
		}

		public NotificationBuilderIn setBuilder(Notification.Builder builder)
		{
			this.builder = builder;
			return this;
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public void show()
		{
			Notification notification = builder.build();
			notification.flags = flags;
			notificationManager.notify(notifyId, notification);
		}
	}

	public void cancel(int notifyId)
	{
		notificationManager.cancel(notifyId);
	}

	public void cancleAll()
	{
		notificationManager.cancelAll();
	}
}

