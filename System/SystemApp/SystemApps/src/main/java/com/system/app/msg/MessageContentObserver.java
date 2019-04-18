package com.system.app.msg;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;

/**
 * 数据库观察者
 * 自己发给自己，回调了5次
 * 仅收短信，也有连续4条【而且的确是最新的一条，因此需要去重处理】
 *
 * @author yline 2019/4/18 -- 9:13
 */
public class MessageContentObserver extends ContentObserver {
    private static final String MSG_URI = "content://sms/";

    public static MessageContentObserver register(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse(MSG_URI);

        MessageContentObserver observer = new MessageContentObserver(SDKManager.getHandler(), resolver);
        resolver.registerContentObserver(uri, true, observer);

        return observer;
    }

    public static void unregister(MessageContentObserver observer) {
        observer.mResolver.unregisterContentObserver(observer);
    }

    private ContentResolver mResolver;
    private OnMessageCallback mOnMessageCallback;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MessageContentObserver(Handler handler, ContentResolver resolver) {
        super(handler);
        this.mResolver = resolver;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        LogUtil.v("selfChange = " + selfChange);

        Uri uri = Uri.parse(MSG_URI);
        Cursor cursor = mResolver.query(uri, new String[]{"address", "date", "body", "type"}, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String body = cursor.getString(cursor.getColumnIndex("body"));

                LogUtil.e(String.format("address = %s, date = %s, type = %s, body = %s", address, date, type, body));
                if (null != mOnMessageCallback) {
                    mOnMessageCallback.onObserver(body);
                }
            }
            cursor.close();
        }
    }

    public void setOnMessageCallback(OnMessageCallback callback) {
        mOnMessageCallback = callback;
    }

    public interface OnMessageCallback {
        /**
         * 短信
         *
         * @param msg 短信
         */
        void onObserver(String msg);
    }
}
