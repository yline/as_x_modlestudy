package com.utils;

import android.annotation.SuppressLint;

import java.util.Calendar;

/**
 * 通过时间戳的类
 * @author YLine
 *         <p/>
 *         2016年7月17日 上午12:43:06
 */
public class TimeStampUtil
{

	public TimeStampUtil()
	{
		/** 实例化失败 */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获取当前时间戳
	 * @return 1451823266000
	 */
	public static long getCurrentStamp()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 获取前时间戳与当前时间戳的差
	 * @param oldTime 前时间戳
	 * @return 差(s)
	 */
	public static long getDiffStamp(long oldTime)
	{
		return getCurrentStamp() - oldTime;
	}

	/**
	 * 是否超时
	 * @param oldTime   旧的时间戳
	 * @param limitTime 限制的时间(单位ms)
	 * @return true(超时)
	 */
	public static boolean isStampTimeOut(long oldTime, long limitTime)
	{
		return getDiffStamp(oldTime) > limitTime ? true : false;
	}

	/**
	 * 获取一个标准的时间
	 * @param time 1451828457000
	 * @return 2016-01-03 21:40:57
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStandard(long time)
	{
		java.util.Date date = new java.util.Date(time);
		java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}

	/**
	 * 年份
	 * @param time 1451825885000
	 * @return 2016    (2016/1/3 20:58:5)
	 */
	public static int getYear(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.YEAR);
	}

	/**
	 * 月份
	 * @param time 1451825885000
	 * @return 1    (2016/1/3 20:58:5)
	 */
	public static int getMonth(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.MONTH) + 1;
	}

	/**
	 * 日期
	 * @param time 1451825885000
	 * @return 3    (2016/1/3 20:58:5)
	 */
	public static int getDay(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 小时
	 * @param time 1451825885000
	 * @return 20    (2016/1/3 20:58:5)
	 */
	public static int getHour(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 分钟
	 * @param time 1451825885000
	 * @return 58    (2016/1/3 20:58:5)
	 */
	public static int getMinute(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.MINUTE);
	}

	/**
	 * 秒钟
	 * @param time 1451825885000
	 * @return 5    (2016/1/3 20:58:5)
	 */
	public static int getSecond(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.SECOND);
	}

	/**
	 * 礼拜几,英式计算,即Sunday算作 1
	 * @param time
	 * @return
	 */
	public static int getDayOfWeekEnglish(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		return date.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 中午礼拜几
	 * @param time 时间戳
	 * @return
	 */
	public static int getDayOfWeek(long time)
	{
		Calendar date = Calendar.getInstance();
		date.setTime(new java.util.Date(time));
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1)
		{
			return 7;
		}
		else
		{
			return dayOfWeek - 1;
		}
	}
}
