package com.sqlite.green.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.sqlite.green.common.AbstractDao;
import com.sqlite.green.common.Property;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 网络请求表 的 操作
 * @author yline 2017/9/6 -- 20:01
 * @version 1.0.0
 */
public class NetCacheModelDao extends AbstractDao<String, NetCacheModel> {
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
        Object headerObject = byteToObject(resultHeader);

        byte[] resultData = cursor.isNull(Table.ResultData.ordinal) ? null : cursor.getBlob(Table.ResultData.ordinal);
        Object dataObject = byteToObject(resultData);

        return new NetCacheModel(requestUrl, requestTag, headerObject, dataObject);
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, NetCacheModel model) {
        String requestUrl = model.getRequestUrl();
        if (null != requestUrl) {
            stmt.bindString(1 + Table.RequestUrl.ordinal, requestUrl);
        }/*else {
            stmt.bindNull(1 + Table.RequestUrl.ordinal);
        }*/

        String requestTag = model.getRequestTag();
        if (null != requestTag) {
            stmt.bindString(1 + Table.RequestTag.ordinal, requestTag);
        }



        Object resultHeader = model.getResultHeader();
        byte[] headerBytes = objectToByte(resultHeader);
        if (null != headerBytes) {
            stmt.bindBlob(1 + Table.ResultHeader.ordinal, headerBytes);
        }

        Object resultData = model.getResultData();
        byte[] dataBytes = objectToByte(resultData);
        if (null != dataBytes) {
            stmt.bindBlob(1 + Table.ResultData.ordinal, dataBytes);
        }
    }

    public static byte[] objectToByte(Object object) {
        if (null != object && object instanceof Serializable) {
            ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(baoStream);
                objectOutputStream.writeObject(object);
                return baoStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != objectOutputStream) {
                        objectOutputStream.close();
                    }
                    baoStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object byteToObject(byte[] bytes) {
        if (null != bytes || bytes.length != 0) {
            ByteArrayInputStream baiStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(baiStream);
                return objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != objectInputStream) {
                        objectInputStream.close();
                    }
                    baiStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
