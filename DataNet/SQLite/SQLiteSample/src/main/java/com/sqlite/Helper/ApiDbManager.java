package com.sqlite.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yline.log.LogFileUtil;

/**
 * 通过Google提供的api的方式进行数据操作
 * @author yline 1 --> 16:57
 * @version 1.0.0
 */
public class ApiDbManager
{
	private static final String TAG = "ApiDbManager";

	/** 表名 */
	private static final String TABLE_NAME = "PersonApi";

	/** id */
	private static final String ID = "_id";

	/** name */
	private static final String NAME = "name";

	/** age */
	private static final String AGE = "age";

	private MySQLiteHelper sqLiteHelper;

	private SQLiteDatabase database;

	private ApiDbManager()
	{
	}

	public static ApiDbManager getInstance()
	{
		return ApiDbManagerHolder.sInstance;
	}

	private static class ApiDbManagerHolder
	{
		private static final ApiDbManager sInstance = new ApiDbManager();
	}

	public void init(Context context)
	{
		createDataBase(context);
	}

	private void createDataBase(Context context)
	{
		sqLiteHelper = new MySQLiteHelper(context);
		database = sqLiteHelper.getReadableDatabase();
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
		LogFileUtil.v(TAG, "insert -> " + "id = " + id + ",name = " + name + ",age = " + age);

		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(NAME, name);
		values.put(AGE, age);

		database = sqLiteHelper.getReadableDatabase();
		long result = database.insert(TABLE_NAME, null, values);
		LogFileUtil.v(TAG, "insert -> result -> " + result);

		database.close();
	}

	public void deleteById(int id)
	{
		LogFileUtil.v(TAG, "deleteById -> " + id);

		database = sqLiteHelper.getReadableDatabase();
		int result = database.delete(TABLE_NAME, ID + "=" + id, null);
		LogFileUtil.v(TAG, "deleteById -> result -> " + result);

		database.close();
	}

	public void updateById(int id, String name, int age)
	{
		LogFileUtil.v(TAG, "updateById -> " + "id = " + id + ",name = " + name + ",age = " + age);

		ContentValues values = new ContentValues();
		values.put(NAME, name);
		values.put(AGE, age);

		database = sqLiteHelper.getReadableDatabase();
		int result = database.update(TABLE_NAME, values, ID + "=?", new String[]{id + ""});
		LogFileUtil.v(TAG, "updateById -> result -> " + result);

		database.close();
	}
}
