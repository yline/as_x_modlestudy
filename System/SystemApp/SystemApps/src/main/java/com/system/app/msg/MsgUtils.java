package com.system.app.msg;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import com.system.app.activity.AppConstant;
import com.system.app.MainApplication;
import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgUtils {
    private static final String MSG_URI = "content://sms/";

    /**
     * 发送短信
     *
     * @param phone   手机号
     * @param content 输入内容
     */
    public static void sendMessage(String phone, String content) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> textList = smsManager.divideMessage(content);
        for (String text : textList) {
            smsManager.sendTextMessage(phone, null, text, null, null);
        }
    }

    /**
     * 读取所有短信
     *
     * @param context 上下文
     */
    public static void readMessage(Context context) {
        Uri uri = Uri.parse(MSG_URI);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "body", "type"}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String body = cursor.getString(cursor.getColumnIndex("body"));

                LogUtil.v(String.format("address = %s, date = %s, type = %s, body = %s", address, date, type, body));
            }
            cursor.close();
        }
    }

    /**
     * 短信内容，解析出，验证码
     *
     * @param message 短信内容
     * @return 验证码
     */
    public static String parseCodeFromMessage(String message) {
        try {
            Pattern pattern6 = Pattern.compile("(\\d{6})"); // 提取六位数字
            Matcher matcher6 = pattern6.matcher(message); // 进行匹配
            if (matcher6.find()) {
                LogUtil.v("result = " + matcher6.group(0));
                return matcher6.group(0);
            }

            Pattern pattern4 = Pattern.compile("(\\d{4})"); // 提取四位数字
            Matcher matcher4 = pattern4.matcher(message); // 进行匹配
            if (matcher4.find()) {
                return matcher4.group(0);
            }

            return "";
        } catch (Exception ex) {
            return "";
        }
    }
}
