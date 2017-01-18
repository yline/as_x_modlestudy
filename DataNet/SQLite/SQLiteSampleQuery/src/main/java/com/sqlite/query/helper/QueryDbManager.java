package com.sqlite.query.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sqlite.query.bean.Person;
import com.yline.log.LogFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 如果数据适配到ListView类似Adapter时,可以使用: SimpleCursorAdapter or CursorAdapter
 * @author yline 2017/1/17 --> 10:37
 * @version 1.0.0
 */
public class QueryDbManager
{
	private static final String TAG = "QueryDbManager";

	private MySQLiteHelper sqLiteHelper;

	private SQLiteDatabase database;

	private QueryDbManager()
	{
	}

	public static QueryDbManager getInstance()
	{
		return QueryDbManagerHolder.sInstance;
	}

	private static class QueryDbManagerHolder
	{
		private static final QueryDbManager sInstance = new QueryDbManager();
	}

	public void init(Context context)
	{
		sqLiteHelper = new MySQLiteHelper(context);
	}

	/** 插入size条,测试数据 */
	public void insert(int size)
	{
		LogFileUtil.v(TAG, "insert -> " + "test data");

		database = sqLiteHelper.getReadableDatabase();

		for (int i = 0; i < size; i++)
		{
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.getID(), i);
			values.put(MySQLiteHelper.getNAME(), "name " + i);
			values.put(MySQLiteHelper.getAGE(), new Random().nextInt(size));

			database.insert(MySQLiteHelper.getTableName(), null, values);
		}

		database.close();
	}

	public List<Person> queryAll()
	{
		String querySql = String.format("select * from %s", MySQLiteHelper.getTableName());
		Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(querySql, null);

		List<Person> resultList = new ArrayList<>();
		if (null != cursor)
		{
			while (cursor.moveToNext())
			{
				int id = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.getID()));
				String name = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.getNAME()));
				int age = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.getAGE()));

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

	public List<Person> queryAllByApi()
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
		Cursor cursor = sqLiteHelper.getReadableDatabase().query(MySQLiteHelper.getTableName(), null,
				MySQLiteHelper.getAGE() + ">10", null, null, null, MySQLiteHelper.getID());

		List<Person> resultList = new ArrayList<>();
		if (null != cursor)
		{
			while (cursor.moveToNext())
			{
				int id = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.getID()));
				String name = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.getNAME()));
				int age = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.getAGE()));

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
}
