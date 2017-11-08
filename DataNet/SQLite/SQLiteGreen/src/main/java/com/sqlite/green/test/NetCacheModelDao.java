package com.sqlite.green.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sqlite.green.common.AbstractSafelyDao;
import com.sqlite.green.common.Property;

/**
 * 网络请求表 的 操作
 * @author yline 2017/9/6 -- 20:01
 * @version 1.0.0
 */
public class NetCacheModelDao extends AbstractSafelyDao<String, NetCacheModel> {
    private static final String TABLE_NAME = "NetCacheModel";

    public static class Table {
        // 请求的参数
        public final static Property RequestUrl = new Property(0, String.class, "reqUrl", true, "RequestUrl");
        public final static Property RequestTag = new Property(1, String.class, "reqTag", false, "RequestTag");

        // 返回的数据
        public final static Property ResultHeader = new Property(2, Object.class, "resultHeader", false, "ResultTag");
        public final static Property ResultData = new Property(3, Object.class, "resultData", false, "ResultData");

        // Attach 防止意外情况列举两列
        public final static Property attachRequest = new Property(4, String.class, "attachRequest", false, "AttachRequest");
        public final static Property attachResult = new Property(5, String.class, "attachResult", false, "AttachResult");
    }

    public NetCacheModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME,
                new Property[]{Table.RequestUrl, Table.RequestTag, Table.ResultHeader, Table.ResultData, Table.attachRequest, Table.attachResult},
                new Property[]{Table.RequestUrl});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s text primary key, %s text, %s blob, %s blob, %s text, %s text);",
                constraint, TABLE_NAME,
                Table.RequestUrl.columnName, Table.RequestTag.columnName, Table.ResultHeader.columnName,
                Table.ResultData.columnName, Table.attachRequest.columnName, Table.attachResult.columnName);
        db.execSQL(sql);
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public String getKey(NetCacheModel model) {
        return model.getRequestUrl();
    }

    @Override
    protected String readKey(Cursor cursor) {
        String key = cursor.isNull(Table.RequestUrl.ordinal) ? null : cursor.getString(Table.RequestUrl.ordinal);
        return key;
    }

    @Override
    protected NetCacheModel readModel(Cursor cursor) {
        String requestUrl = cursor.isNull(Table.RequestUrl.ordinal) ? null : cursor.getString(Table.RequestUrl.ordinal);
        String requestTag = cursor.isNull(Table.RequestTag.ordinal) ? null : cursor.getString(Table.RequestTag.ordinal);
        byte[] resultHeader = cursor.isNull(Table.ResultHeader.ordinal) ? null : cursor.getBlob(Table.ResultHeader.ordinal);
        byte[] resultData = cursor.isNull(Table.ResultData.ordinal) ? null : cursor.getBlob(Table.ResultData.ordinal);

        return new NetCacheModel(requestUrl, requestTag, resultHeader, resultData);
    }

    @Override
    protected boolean bindValues(SQLiteStatement stmt, NetCacheModel model) {
        String requestUrl = model.getRequestUrl();
        if (null != requestUrl) {
            stmt.bindString(1 + Table.RequestUrl.ordinal, requestUrl);
        }

        String requestTag = model.getRequestTag();
        if (null != requestTag) {
            stmt.bindString(1 + Table.RequestTag.ordinal, requestTag);
        }

        byte[] resultHeader = model.getResultHeader();
        if (null != resultHeader) {
            stmt.bindBlob(1 + Table.ResultHeader.ordinal, resultHeader);
        }

        byte[] resultData = model.getResultData();
        if (null != resultData) {
            stmt.bindBlob(1 + Table.ResultData.ordinal, resultData);
        }

        return true;
    }
}
