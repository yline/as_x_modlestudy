package com.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.yline.log.LogFileUtil;

/**
 * @author yline 2016/9/4 --> 20:33
 * @version 1.0.0
 */
public class APNManager
{
	/** APNManager */
	public static final String TAG = "APNActivity -> APNManager";
	
	/** 所有的APN配配置信息位置;用于查找 */
	private static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
	
	/** 当前的APN位置;用于更新 */
	private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	/** 限定符,限定只查找这几项;本工具去掉 */
	private static String[] projection = {"_id", "apn", "type", "current", "proxy", "port", "name"};
	
	/** 打印所有APN */
	public static void getAPNList(final Context context)
	{
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cr = contentResolver.query(APN_TABLE_URI, null, null, null, null);
		
		if (cr != null && cr.moveToFirst())
		{
			do
			{
				log(cr);
			} while (cr.moveToNext());
			
			cr.close();
		}
		else
		{
			LogFileUtil.v(TAG, "getAPNList Cursor is null");
		}
	}
	
	/** 打印可用的APN */
	public static void getAvailableAPNList(final Context context)
	{
		// current不为空表示可以使用的APN  
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cr = contentResolver.query(APN_TABLE_URI, null, "current is not null", null, null);
		
		if (cr != null && cr.moveToFirst())
		{
			do
			{
				log(cr);
			} while (cr.moveToNext());
			
			cr.close();
		}
		else
		{
			LogFileUtil.v(TAG, "getAvailableAPNList Cursor is null");
		}
	}
	
	/** 打印当前APN,部分信息,并获取相应的id */
	public static String getCurrentApnId(Context context)
	{
		ContentResolver resoler = context.getContentResolver();
		// String[] projection = new String[] { "_id" };  
		Cursor cur = resoler.query(PREFERRED_APN_URI, projection, null, null, null);
		
		String apnId = null;
		if (cur != null && cur.moveToFirst())
		{
			log(cur);
			apnId = cur.getString(cur.getColumnIndex("_id"));
			LogFileUtil.v(TAG, "getCurApnId:" + apnId);
		}
		else
		{
			LogFileUtil.v(TAG, "getCurrentApnId Cursor is null");
		}
		
		return apnId;
	}
	
	/** 打印WAP APN的部分信息 */
	public static void getWapApnId(Context context)
	{
		ContentResolver contentResolver = context.getContentResolver();
		// 查询cmwapAPN  
		Cursor cur = contentResolver.query(APN_TABLE_URI, projection, "apn = \'cmwap\' and current = 1", null, null);
		// wap APN 端口不为空  
		if (cur != null && cur.moveToFirst())
		{
			do
			{
				log(cur);
			} while (cur.moveToNext());
		}
	}
	
	/** 切换apn需要一定时间，需要等待几秒，与机子性能有关 */
	public static boolean switchApnByName(Context context, String name)
	{
		Cursor cursor = context.getContentResolver().query(APN_TABLE_URI, null, "name = ?", new String[]{name}, null);
		
		if (null != cursor && cursor.moveToFirst())
		{
			LogFileUtil.i(TAG, "aim cursor length = " + cursor.getColumnNames().length);
			log(cursor);
			setCurrentApn(context, cursor.getString(cursor.getColumnIndex("_id")));
			
			return true;
		}
		else
		{
			LogFileUtil.e(TAG, "switchApnByName failed, aim cursor is null");
			return false;
		}
	}
	
	/** 设置当前APN */
	public static void setCurrentApn(Context context, String id)
	{
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("apn_id", id);
		int row = resolver.update(PREFERRED_APN_URI, values, null, null);
		LogFileUtil.v(TAG, "setCurrentApn row = " + row + ",id = " + id);
	}
	
	/** 打印一条Cursor中所有信息 */
	private static void log(Cursor cursor)
	{
		String[] strs = cursor.getColumnNames();
		
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strs)
		{
			stringBuffer.append(string + " = " + cursor.getString(cursor.getColumnIndex(string)) + "; ");
		}
		LogFileUtil.v(TAG, stringBuffer.toString());
		LogFileUtil.v(TAG, "-----------------------------------------------");
	}
}
