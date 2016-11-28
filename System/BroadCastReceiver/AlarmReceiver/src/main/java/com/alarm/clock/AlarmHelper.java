package com.alarm.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.yline.log.LogFileUtil;

import java.util.Calendar;

/**
 * Created by yline on 2016/11/28.
 */
public class AlarmHelper
{
	public static final String ALARM_ACTION = "com.alarm.clock";

	private static final String TAG = "AlarmHelper";

	private AlarmHelper()
	{
	}

	public static AlarmHelper getInstance()
	{
		return AlarmHelperHolder.sInstance;
	}

	private static class AlarmHelperHolder
	{
		private static AlarmHelper sInstance = new AlarmHelper();
	}

	public static class Builder
	{
		private AlarmManager alarmManager;

		private Calendar calendar;

		private int year;

		private int month;

		private int day;

		private int hour;

		private int minute;

		private int second;

		private int requestCode = 0;

		/** 间隔时间 */
		private long interval = 0;

		private String message = "闹钟响了";

		private boolean isVibrator;

		private boolean isRepeat;

		public Builder()
		{
			calendar = Calendar.getInstance();

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
			second = calendar.get(Calendar.SECOND);
		}

		public Builder setYear(int year)
		{
			this.year = year;
			return this;
		}

		public Builder setMonth(int month)
		{
			this.month = month;
			return this;
		}

		public Builder setDay(int day)
		{
			this.day = day;
			return this;
		}

		public Builder setHour(int hour)
		{
			this.hour = hour;
			return this;
		}

		public Builder setMinute(int minute)
		{
			this.minute = minute;
			return this;
		}

		public Builder setSecond(int second)
		{
			this.second = second;
			return this;
		}

		public Builder setRequestCode(int requestCode)
		{
			this.requestCode = requestCode;
			return this;
		}

		public Builder setInterval(long interval)
		{
			this.interval = interval;
			return this;
		}

		public Builder setMessage(String message)
		{
			this.message = message;
			return this;
		}

		public Builder setVibrator(boolean vibrator)
		{
			isVibrator = vibrator;
			return this;
		}

		public Builder setRepeat(boolean repeat)
		{
			isRepeat = repeat;
			return this;
		}

		public void create(Context context)
		{
			alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			calendar.set(year, month, day, hour, minute, second);
			Intent intent = new Intent(ALARM_ACTION);
			intent.putExtra("interval", interval);
			intent.putExtra("message", message);
			intent.putExtra("requestCode", requestCode);
			intent.putExtra("isVibrator", isVibrator);

			LogFileUtil.v("year = " + year + ",month = " + month + ",day = " + day + ",hour = " + hour + ",minute = " + minute + ",second = " + second);
			LogFileUtil.v("interval = " + interval + ",message = " + message + ",requestCode = " + requestCode + ",isVibrator = " + isVibrator);

			int i = -1;
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				i = 1;
				alarmManager.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
			}
			else
			{
				if (isRepeat)
				{
					i = 2;
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
				}
				else
				{
					i = 3;
					alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
				}
			}
			LogFileUtil.v("current = " + System.currentTimeMillis() + ",calendar = " + calendar.getTimeInMillis() + ",i = " + i);
		}
	}

	public void setAlarm(Context context, Intent intent)
	{
		int requestCode = intent.getIntExtra("requestCode", 0);
		long interval = intent.getLongExtra("interval", 0);
		LogFileUtil.v(TAG, "requestCode = " + requestCode + ",interval = " + interval);
		if (0 != interval)
		{
			setAlarmTime(context, intent, System.currentTimeMillis() + interval, interval, requestCode);
		}
	}

	/**
	 * @param context
	 * @param intent
	 * @param timeInMillis 闹钟时间
	 * @param interval     间隔时间
	 * @param requestCode
	 */
	private void setAlarmTime(Context context, Intent intent, long timeInMillis, long interval, int requestCode)
	{
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			alarmManager.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, pendingIntent);
		}
	}

	public void cancelAlarm(Context context, String action, int id)
	{
		Intent intent = new Intent(action);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
}
