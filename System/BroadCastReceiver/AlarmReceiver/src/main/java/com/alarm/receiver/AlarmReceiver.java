package com.alarm.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.alarm.MainApplication;
import com.yline.base.BaseReceiver;

import java.util.Calendar;

/**
 * 能够准时的收到定下的闹钟
 *
 * @author yline 2016/11/28 --> 21:22
 * @version 1.0.0
 */
public class AlarmReceiver extends BaseReceiver {
	public static final String ALARM_ACTION = "com.alarm.clock";
	
	public static void setClock(Context context, int hour, int min, boolean isRepeat, int requestCode) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		long timeMillis = calendar.getTimeInMillis();
		
		if (null != alarmManager) {
			Intent intent = new Intent(ALARM_ACTION);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				alarmManager.setWindow(AlarmManager.RTC_WAKEUP, timeMillis, 0, pendingIntent);
			} else {
				if (isRepeat) {
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeMillis, 1000, pendingIntent);
				} else {
					alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
				}
			}
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		setAlarm(context, intent);
		MainApplication.toast("成功接收到系统闹钟广播");
	}
	
	public void setAlarm(Context context, Intent intent) {
		int requestCode = intent.getIntExtra("requestCode", 0);
		long interval = intent.getLongExtra("interval", 0);
		if (0 != interval) {
			setAlarmTime(context, intent, System.currentTimeMillis() + interval, interval, requestCode);
		}
	}
	
	/**
	 * @param timeInMillis 闹钟时间
	 * @param interval     间隔时间
	 */
	private void setAlarmTime(Context context, Intent intent, long timeInMillis, long interval, int requestCode) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			alarmManager.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, pendingIntent);
		}
	}
	
	public void cancelAlarm(Context context, String action, int id) {
		Intent intent = new Intent(action);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
}
