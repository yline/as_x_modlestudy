package com.sqlite.green.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sqlite.green.common.AbstractDao;
import com.sqlite.green.common.Property;

/**
 * @author yline 2017/9/6 -- 20:01
 * @version 1.0.0
 */
public class MsgDao extends AbstractDao<String, Msg>
{
	private static final String TABLE_NAME = "Msg";
	
	public static class Table
	{
		public final static Property UserId = new Property(0, String.class, "id", true, "UserId");
		
		public final static Property UserName = new Property(1, String.class, "name", false, "UserName");
		
		public final static Property UserNickName = new Property(2, String.class, "nickname", false, "UserNickName");
		
		public final static Property PhoneNumber = new Property(3, String.class, "number", false, "PhoneNumber");
		
		public final static Property PhoneTag = new Property(4, String.class, "tag", false, "PhoneTag");
	}
	
	public MsgDao(SQLiteDatabase db)
	{
		super(db, TABLE_NAME,
				new Property[]{Table.UserId, Table.UserName, Table.UserNickName, Table.PhoneNumber, Table.PhoneTag},
				new Property[]{Table.UserId});
	}
	
	public static void createTable(SQLiteDatabase db, boolean ifNotExists)
	{
		String constraint = ifNotExists ? "if not exists" : "";
		String sql = String.format("create table %s %s(%s text primary key, %s text, %s text, %s text, %s text);",
				constraint, TABLE_NAME,
				Table.UserId.columnName, Table.UserName.columnName, Table.UserNickName.columnName,
				Table.PhoneNumber.columnName, Table.PhoneTag.columnName);
		db.execSQL(sql);
	}
	
	public static void dropTable(SQLiteDatabase db, boolean ifExists)
	{
		String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
		db.execSQL(sql);
	}
	
	@Override
	public String getKey(Msg msg)
	{
		return msg.getUserId();
	}
	
	@Override
	protected String readKey(Cursor cursor)
	{
		String key = cursor.isNull(Table.UserId.ordinal) ? null : cursor.getString(Table.UserId.ordinal);
		return key;
	}
	
	@Override
	protected Msg readModel(Cursor cursor)
	{
		String userId = cursor.isNull(Table.UserId.ordinal) ? null : cursor.getString(Table.UserId.ordinal);
		String userName = cursor.isNull(Table.UserName.ordinal) ? null : cursor.getString(Table.UserName.ordinal);
		String userNickName = cursor.isNull(Table.UserNickName.ordinal) ? null : cursor.getString(Table.UserNickName.ordinal);
		String phoneNumber = cursor.isNull(Table.PhoneNumber.ordinal) ? null : cursor.getString(Table.PhoneNumber.ordinal);
		String phoneTag = cursor.isNull(Table.PhoneTag.ordinal) ? null : cursor.getString(Table.PhoneTag.ordinal);
		return new Msg(userId, userName, userNickName, phoneNumber, phoneTag);
	}
	
	@Override
	protected void bindValues(SQLiteStatement stmt, Msg msg)
	{
		String userId = msg.getUserId();
		if (null != userId)
		{
			stmt.bindString(1 + Table.UserId.ordinal, userId);
		}
		String userName = msg.getUserName();
		if (null != userId)
		{
			stmt.bindString(1 + Table.UserName.ordinal, userName);
		}
		
		String userNickName = msg.getUserNickName();
		if (null != userId)
		{
			stmt.bindString(1 + Table.UserNickName.ordinal, userNickName);
		}
		
		String phoneNumber = msg.getPhoneNumber();
		if (null != userId)
		{
			stmt.bindString(1 + Table.PhoneNumber.ordinal, phoneNumber);
		}
		
		String phoneTag = msg.getPhoneTag();
		if (null != userId)
		{
			stmt.bindString(1 + Table.PhoneTag.ordinal, phoneTag);
		}
	}
}
