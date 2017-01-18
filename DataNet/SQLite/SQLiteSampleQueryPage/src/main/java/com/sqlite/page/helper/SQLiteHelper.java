package com.sqlite.page.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yline.log.LogFileUtil;

/**
 * 1,创建数据库
 * 2,创建数据表,并储存数据表的字段
 * @author yline 2017/1/18 --> 16:02
 * @version 1.0.0
 */
public class SQLiteHelper extends SQLiteOpenHelper
{
	private static final String TAG = "SQLiteHelper";

	private static final String DATABASE_NAME = "SampleQueryPage.db";

	private static final int DATABASE_VERSION = 1;

	// 数据表
	private static final String TABLE_NAME = "PersonPage";

	private static final String ID = "_id";

	private static final String NAME = "name";

	private static final String AGE = "age";

	public SQLiteHelper(Context context)
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

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
		LogFileUtil.v(TAG, "onOpen");
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
