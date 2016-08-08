package com.system.app.msg;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;

import com.system.app.activity.AppConstant;
import com.system.app.activity.MainApplication;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;

public class MsgHelper
{
	private static final String MSG_URI = "content://sms/";

	private static final String ADDARESS = "address";

	private static final String DATE = "date";

	private static final String TYPE = "type";

	private static final String BODY = "body";

	/**
	 * 发送短信
	 */
	public void sendMsg()
	{
		final String number = "563850";
		final String content = "这是发送的短信内容";

		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> contents = smsManager.divideMessage(content);
		for (String str : contents)
		{
			smsManager.sendTextMessage(number, null, str, null, null);
		}

		LogFileUtil.v(AppConstant.TAG_MSG, "MsgHelper -> sendMsg success");
	}

	/**
	 * 读取系统短信
	 */
	public void readMsgAll(Context context)
	{
		Uri uri = Uri.parse(MSG_URI);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{ADDARESS, DATE, TYPE, BODY}, null, null, null);
		while (cursor.moveToNext())
		{
			String address = cursor.getString(cursor.getColumnIndex(ADDARESS));
			String date = cursor.getString(cursor.getColumnIndex(DATE));
			String type = cursor.getString(cursor.getColumnIndex(TYPE));
			String body = cursor.getString(cursor.getColumnIndex(BODY));

			LogFileUtil.v(AppConstant.TAG_MSG, String.format("%s=%s,%s=%s,%s=%s,%s=%s\n", ADDARESS, address, DATE, date, TYPE, type, BODY, body));
		}
		cursor.close();
	}

	public void registerMsgobserver(Context context)
	{
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse(MSG_URI);

		//接受内容观察者数据
		resolver.registerContentObserver(uri, true, new MsgObserver(new Handler()));
	}

	private class MsgObserver extends ContentObserver
	{

		public MsgObserver(Handler handler)
		{
			super(handler);
		}

		/**
		 * 自己给自己发送一次短信,会调用比较多的次数(4次)
		 * 可以利用type和selfChange来判断
		 * <p/>
		 * 当内容观察者 观察到数据库的变化时，调用这个方法
		 * 实际上为，观察到消息邮箱里面有一条数据库内容变化的通知
		 */
		@Override
		public void onChange(boolean selfChange)
		{
			super.onChange(selfChange);
			LogFileUtil.v(AppConstant.TAG_MSG, "selfChange = " + selfChange);

			ContentResolver resolver = MainApplication.getApplication().getContentResolver();
			Uri uri = Uri.parse(MSG_URI);
			Cursor cursor = resolver.query(uri, new String[]{"address", "date", "body", "type"}, null, null, null);

			cursor.moveToFirst();
			String address = cursor.getString(cursor.getColumnIndex(ADDARESS));
			String date = cursor.getString(cursor.getColumnIndex(DATE));
			String type = cursor.getString(cursor.getColumnIndex(TYPE));
			String body = cursor.getString(cursor.getColumnIndex(BODY));

			LogFileUtil.v(AppConstant.TAG_MSG, String.format("%s=%s,%s=%s,%s=%s,%s=%s\n", ADDARESS, address, DATE, date, TYPE, type, BODY, body));

			cursor.close();
		}
	}
}
