package com.yline.provider.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yline.application.SDKManager;
import com.yline.provider.table.TestTable;

/**
 * @author yline 2020-04-19 -- 22:31
 */
public class TestDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "test.db";

    private volatile static TestDBHelper mDbHelper;

    public static TestDBHelper getInstance() {
        if (null == mDbHelper) {
            synchronized (TestDBHelper.class) {
                if (null == mDbHelper) {
                    mDbHelper = new TestDBHelper(SDKManager.getApplication());
                }
            }
        }
        return mDbHelper;
    }

    private TestDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if (null != mDbHelper) {
            throw new RuntimeException("TestDBHelper cannot construct");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TestTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TestTable.dropTable(db);
        onCreate(db);
    }
}
