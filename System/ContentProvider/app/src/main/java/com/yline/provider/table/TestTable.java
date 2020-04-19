package com.yline.provider.table;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 某张表的操作集中
 *
 * @author yline 2020-04-19 -- 19:25
 */
public class TestTable {
    private static final String TABLE_NAME = "test";

    private static final String CONTENT_AUTHORITY = "com.yline.test.provider";
    private static final String PATH_TEST = "test";

    private static final String _ID = BaseColumns._ID;
    private static final String COLUMN_NAME = "name";

    private static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", CONTENT_AUTHORITY, PATH_TEST));

    /**
     * 创建表
     */
    public static void createTable(SQLiteDatabase db) {
        final String createOrder = String.format("create table %s(%s text primary key, %s text not null);",
                TABLE_NAME, _ID, COLUMN_NAME);
        db.execSQL(createOrder);
    }

    /**
     * 销毁表
     */
    public static void dropTable(SQLiteDatabase db) {
        final String dropOrder = String.format("drop table if exists %s", TABLE_NAME);
        db.execSQL(dropOrder);
    }

    /**
     * 路径匹配规则
     */
    public static void attachMatcher(UriMatcher matcher, int code) {
        matcher.addURI(CONTENT_AUTHORITY, PATH_TEST, code);
    }

    public static Cursor query(SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return db.query(TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null);
    }

    public static Uri insert(Context context, SQLiteDatabase db, ContentValues values) {
        long _id = db.insert(TABLE_NAME, null, values);
        if (_id > 0) {
            context.getContentResolver().notifyChange(CONTENT_URI, null);
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
        return null;
    }
}
