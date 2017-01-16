package com.sqlite.query.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2017/1/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper
{
	private static final String TAG = "MySQLiteHelper";

	private static final String DATABASE_NAME = "SampleQuery.db";

	private static final int DATABASE_VERSION = 1;

	// 数据表
	private static final String TABLE_NAME = "MyPerson";

	private static final String ID = "_id";

	private static final String NAME = "name";

	private static final String AGE = "age";

	public MySQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		LogFileUtil.v(TAG, "onCreate");

		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		LogFileUtil.v(TAG, "onUpgrade oldVersion = " + oldVersion + ", newVersion = " + newVersion);
	}

	/** 创建数据表 */
	private void createTable(SQLiteDatabase db)
	{
		String tableSQLite = String.format("create table %s(%s Integer primary key, %s varchar(16), %s Integer)", TABLE_NAME, ID, NAME, AGE);
		db.execSQL(tableSQLite);
	}

	public static String getTableName()
	{
		return TABLE_NAME;
	}

	public static String getID()
	{
		return ID;
	}

	public static String getNAME()
	{
		return NAME;
	}

	public static String getAGE()
	{
		return AGE;
	}
}
