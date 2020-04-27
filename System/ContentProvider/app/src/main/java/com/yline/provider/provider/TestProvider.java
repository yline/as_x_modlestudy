package com.yline.provider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.yline.provider.table.TestTable;
import com.yline.utils.LogUtil;

/**
 * @author yline 2020-04-19 -- 22:31
 */
public class TestProvider extends ContentProvider {

    private static final int CODE_TEST = 1;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        TestTable.attachMatcher(MATCHER, CODE_TEST);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        LogUtil.v("uri = " + uri);

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        LogUtil.v("uri = " + uri);

        final SQLiteDatabase db = TestDBHelper.getInstance().getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_TEST:
                return TestTable.query(db, projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LogUtil.v("uri = " + uri);

        final SQLiteDatabase db = TestDBHelper.getInstance().getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_TEST:
                return TestTable.insert(getContext(), db, values);
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        LogUtil.v("uri = " + uri);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LogUtil.v("uri = " + uri);
        return 0;
    }


}
