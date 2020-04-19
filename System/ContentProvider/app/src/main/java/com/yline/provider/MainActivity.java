package com.yline.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;

import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

/**
 * 这里专职模仿 使用者，因此不能依赖任何其他类
 *
 * @author yline 2020-04-19 -- 22:31
 */
public class MainActivity extends BaseTestActivity {

    private final ContentObserver contentObserver = new ContentObserver(null) {
        @Override
        public boolean deliverSelfNotifications() {
            LogUtil.v("deliverSelfNotifications");
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            LogUtil.v("selfChange = " + selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            LogUtil.v("selfChange = " + selfChange + ", uri = " + uri);
        }
    };

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        getContentResolver().registerContentObserver(UserTable.CONTENT_URI, true, contentObserver);

        addButton("test", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTable.testProvider(MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        getContentResolver().unregisterContentObserver(contentObserver);

        super.onDestroy();
    }

    private static class UserTable {
        private static void testProvider(Context context) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, "peng");
            contentValues.put(_ID, System.currentTimeMillis());
            context.getContentResolver().insert(CONTENT_URI, contentValues);

            Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            try {
                if (null == cursor) {
                    LogUtil.v("cursor is null");
                    return;
                }

                LogUtil.v("total data number = " + cursor.getCount());
                cursor.moveToFirst();
                LogUtil.v("total data number = " + cursor.getString(1));
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }

        private static final String CONTENT_AUTHORITY = "com.yline.test.provider";
        private static final String PATH_TEST = "test";

        private static final String _ID = BaseColumns._ID;
        private static final String COLUMN_NAME = "name";

        private static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", CONTENT_AUTHORITY, PATH_TEST));
    }

}
