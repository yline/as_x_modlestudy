package com.sqlite.green.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sqlite.green.common.AbstractDao;
import com.sqlite.green.common.Property;

/**
 * 简易的表 操作的测试
 *
 * @author yline 2017/9/14 -- 14:35
 * @version 1.0.0
 */
public class SimpleModelDao extends AbstractDao<String, SimpleModel> {
    private static final String TABLE_NAME = "SimpleModel";

    public static class Table {
        public final static Property Key = new Property(0, String.class, "key", true, "Key");

        public final static Property Value = new Property(1, String.class, "value", false, "SimpleModel");
    }

    public SimpleModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME,
                new Property[]{Table.Key, Table.Value},
                new Property[]{Table.Key});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s text primary key, %s text);",
                constraint, TABLE_NAME,
                Table.Key.columnName, Table.Value.columnName);
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public String getKey(SimpleModel value) {
        return value.getKey();
    }

    @Override
    protected String readKey(Cursor cursor) {
        String key = cursor.isNull(Table.Key.ordinal) ? null : cursor.getString(Table.Key.ordinal);
        return key;
    }

    @Override
    protected SimpleModel readModel(Cursor cursor) {
        String key = cursor.isNull(Table.Key.ordinal) ? null : cursor.getString(Table.Key.ordinal);
        String value = cursor.isNull(Table.Value.ordinal) ? null : cursor.getString(Table.Value.ordinal);
        return new SimpleModel(key, value);
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, SimpleModel value) {
        String userId = value.getKey();
        if (null != userId) {
            stmt.bindString(1 + Table.Key.ordinal, userId);
        }

        String userName = value.getValue();
        if (null != userId) {
            stmt.bindString(1 + Table.Value.ordinal, userName);
        }
    }
}
