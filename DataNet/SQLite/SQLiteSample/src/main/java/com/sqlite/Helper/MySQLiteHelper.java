package com.sqlite.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2017/1/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper
{
	private static final String TAG = "SQLiteHelper";

	private static final String DATABASE_NAME = "sample.db";

	private static final int DATABASE_VERSION = 1;

	public MySQLiteHelper(Context context)
	{
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * @param context 上下文
	 * @param name    名称
	 * @param factory null
	 * @param version 版本
	 */
	public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		LogFileUtil.v(TAG, "Construct");
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		LogFileUtil.v(TAG, "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		LogFileUtil.v(TAG, "onUpgrade");
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
		LogFileUtil.v(TAG, "onOpen");
	}
}
