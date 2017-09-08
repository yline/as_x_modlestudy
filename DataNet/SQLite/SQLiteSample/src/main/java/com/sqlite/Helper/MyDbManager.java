package com.sqlite.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yline.log.LogFileUtil;

/**
 * 通过语句的方式,进行数据库操作
 * 注意事项:
 * 1,如果这种方式,数据库表多次建立,程序会崩溃
 * @author yline 2017/1/16 --> 16:15
 * @version 1.0.0
 */
public class MyDbManager
{
	private static final String TAG = "MyDbManager";

	/** 表名 */
	private static final String TABLE_NAME = "PersonMy";

	/** id */
	private static final String ID = "_id";

	/** name */
	private static final String NAME = "name";

	/** age */
	private static final String AGE = "age";

	private MySQLiteHelper sqLiteHelper;

	private MyDbManager()
	{
	}

	public static MyDbManager getInstance()
	{
		return MyDbManagerHolder.sInstance;
	}

	private static class MyDbManagerHolder
	{
		private static final MyDbManager sInstance = new MyDbManager();
	}

	public void init(Context context)
	{
		createDataBase(context);
	}

	private void createDataBase(Context context)
	{
		sqLiteHelper = new MySQLiteHelper(context);
		SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
		String createSql = String.format("create table %s(%s Integer primary key, %s varchar(16), %s Integer not null)", TABLE_NAME, ID, NAME, AGE);
		LogFileUtil.v(TAG, createSql);
		try
		{
			database.execSQL(createSql);
			database.close();
		}
		catch (Exception e)
		{
			LogFileUtil.e(TAG, createSql, e);
		}

	}

	public void insert(int id, String name, int age)
	{
		String insertSql = String.format("insert into %s(%s, %s, %s) values(%s, \"%s\", %s)", TABLE_NAME, ID, NAME, AGE, id, name, age);
		LogFileUtil.v(TAG, insertSql);

		SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
		database.execSQL(insertSql);
		
		database.close();
	}

	public void deleteById(int id)
	{
		String deleteSql = String.format("delete from %s where %s=%s", TABLE_NAME, ID, id);
		LogFileUtil.v(TAG, deleteSql);

		SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
		database.execSQL(deleteSql);
		database.close();
	}

	public void updateById(int id, String name, int age)
	{
		String updateSql = String.format("update %s set %s=\"%s\",%s=%s where %s=%s", TABLE_NAME, NAME, name, AGE, age, ID, id);
		LogFileUtil.v(TAG, updateSql);

		SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
		database.execSQL(updateSql);
		database.close();
	}
}
