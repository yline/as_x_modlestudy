package com.sqlite.page.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sqlite.page.bean.Person;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Api方式 实现 数据库方式
 * @author yline 2017/1/18 --> 16:08
 * @version 1.0.0
 */
public class DbManager
{
	private static final String TAG = "DbManager";

	private SQLiteHelper sqLiteHelper;

	private SQLiteDatabase database;

	private DbManager()
	{
	}

	public static DbManager getInstance()
	{
		return DbManagerHolder.sInstance;
	}

	private static class DbManagerHolder
	{
		private static final DbManager sInstance = new DbManager();
	}

	public void init(Context context)
	{
		sqLiteHelper = new SQLiteHelper(context);
	}

	public void insert(int id, String name, int age)
	{
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.getID(), id);
		values.put(SQLiteHelper.getNAME(), name);
		values.put(SQLiteHelper.getAGE(), age);

		database = sqLiteHelper.getWritableDatabase();

		long result = database.insert(SQLiteHelper.getTableName(), null, values);
		LogFileUtil.v(TAG, "id = " + id + ",name = " + name + ",age = " + age + ",result = " + result);

		database.close();
	}

	/**
	 * 通过 事务,一次性插入多条
	 * @param startId 起点id
	 * @param name
	 * @param age     年龄,随机基本数
	 * @param size    条数
	 */
	public void insertAtSameMoment(int startId, String name, int age, int size)
	{
		database = sqLiteHelper.getWritableDatabase();
		database.beginTransaction();

		Random random = new Random();

		for (int i = 0; i < size; i++)
		{
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.getID(), startId + i);
			values.put(SQLiteHelper.getNAME(), name + i);
			values.put(SQLiteHelper.getAGE(), random.nextInt(age) + i);

			database.insert(SQLiteHelper.getTableName(), null, values);
		}

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}

	public void deleteById(int id)
	{
		database = sqLiteHelper.getWritableDatabase();

		int result = database.delete(SQLiteHelper.getTableName(), SQLiteHelper.getID() + "=" + id, null);
		LogFileUtil.v(TAG, "id = " + id + ",result = " + result);

		database.close();
	}

	public void updateById(int id, String name, int age)
	{
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.getNAME(), name);
		values.put(SQLiteHelper.getAGE(), age);

		database = sqLiteHelper.getReadableDatabase();

		int result = database.update(SQLiteHelper.getTableName(), values, SQLiteHelper.getID() + "=" + id, null);
		LogFileUtil.v(TAG, "id = " + id + ",name = " + name + ",age = " + age + ",result = " + result);

		database.close();
	}

	// public List<Person> queryLimit(){}

	public List<Person> queryAllThroughMinAge(int minAge)
	{
		/**
		 * Query the given table, returning a {@link Cursor} over the result set.
		 *
		 * @param table 表名
		 * @param columns 查询的字段名称 null 查询所有字段
		 * @param selection 查询条件
		 * @param selectionArgs 查询条件的 占位符
		 * @param groupBy 分组条件
		 * @param having 筛选条件
		 * @param orderBy 排序条件
		 */
		Cursor cursor = sqLiteHelper.getWritableDatabase().query(SQLiteHelper.getTableName(), null,
				SQLiteHelper.getAGE() + ">" + minAge, null, null, null, SQLiteHelper.getAGE());

		List<Person> resultList = new ArrayList<>();
		if (null != cursor)
		{
			while (cursor.moveToNext())
			{
				int id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.getID()));
				String name = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getNAME()));
				int age = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.getAGE()));

				resultList.add(new Person(id, name, age));
			}
		}
		else
		{
			LogFileUtil.v(TAG, "cursor is null");
		}

		LogFileUtil.v(TAG, "size = " + resultList.size());

		return resultList;
	}

	public List<Person> queryAllLimit(int page, int pageSize)
	{
		Cursor cursor = sqLiteHelper.getWritableDatabase().query(SQLiteHelper.getTableName(), null,
				null, null, null, null, null, (page * pageSize) + "," + pageSize);

		List<Person> resultList = new ArrayList<>();
		if (null != cursor)
		{
			while (cursor.moveToNext())
			{
				int id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.getID()));
				String name = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getNAME()));
				int age = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.getAGE()));

				resultList.add(new Person(id, name, age));
			}
		}
		else
		{
			LogFileUtil.v(TAG, "cursor is null");
		}

		LogFileUtil.v(TAG, "size = " + resultList.size());

		return resultList;
	}

	public long getCountSize()
	{
		long count = 0;
		Cursor cursor = sqLiteHelper.getWritableDatabase().query(SQLiteHelper.getTableName(), null, null, null, null, null, null);
		if (null != cursor)
		{
			count = cursor.getCount();
		}
		return count;
	}
}
