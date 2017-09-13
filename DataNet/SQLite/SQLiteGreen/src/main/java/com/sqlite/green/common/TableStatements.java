package com.sqlite.green.common;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * SQLiteStatement 管理类
 *
 * @author yline 2017/9/8 -- 19:45
 * @version 1.0.0
 */
public class TableStatements {

    private final String mTableName;
    private final String[] mAllColumns;

    private final SQLiteDatabase mDb;

    private SQLiteStatement countStatement; // 计算 数据表 行数
    private SQLiteStatement insertStatement; // 向 数据表中 插入单条数据
    private SQLiteStatement insertOrReplaceStatement; // insertOrReplace

    public TableStatements(String tableName, SQLiteDatabase db, String[] allColumns) {
        this.mTableName = tableName;
        this.mDb = db;

        this.mAllColumns = allColumns;
    }

    public SQLiteStatement getCountStatement() {
        if (countStatement == null) {
            String sql = String.format("select count(*) from %s", mTableName);
            countStatement = mDb.compileStatement(sql);
        }
        return countStatement;
    }

    public SQLiteStatement getInsertStatement() {
        if (null == insertStatement) {
            String sql = genInsertSql("insert into");
            SQLiteStatement newInsertStatement = mDb.compileStatement(sql);
            synchronized (this) {
                if (null == insertStatement) {
                    insertStatement = newInsertStatement;
                }
            }

            if (insertStatement != newInsertStatement) {
                newInsertStatement.close();
            }
        }
        return insertStatement;
    }

    public SQLiteStatement getInsertOrReplaceStatement() {
        if (insertOrReplaceStatement == null) {
            String sql = genInsertSql("insert or replace into");
            SQLiteStatement newInsertOrReplaceStatement = mDb.compileStatement(sql);
            synchronized (this) {
                if (insertOrReplaceStatement == null) {
                    insertOrReplaceStatement = newInsertOrReplaceStatement;
                }
            }
            if (insertOrReplaceStatement != newInsertOrReplaceStatement) {
                newInsertOrReplaceStatement.close();
            }
        }
        return insertOrReplaceStatement;
    }

    /**
     * @return Such as "insert into ?(?, ?, ?) values(?, ?, ?)"
     */
    public String genInsertSql(String header) {
        StringBuilder stringBuilder = new StringBuilder(header + " ");
        stringBuilder.append('"').append(mTableName).append('"').append(" (");

        int length = mAllColumns.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append('"').append(mAllColumns[i]).append('"');
            if (i < length - 1) {
                stringBuilder.append(',');
            }
        }

        stringBuilder.append(") values(");

        for (int i = 0; i < length; i++) {
            stringBuilder.append('?');
            if (i < length - 1) {
                stringBuilder.append(',');
            }
        }
        stringBuilder.append(')');

        return stringBuilder.toString();
    }
}
