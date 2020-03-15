package com.yline.runtime.xutils.lib.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Log工具，类似android.util.Log
 * tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)
 */
public class LogIoc {
    private static final boolean isDebug = true; // log 开关

    private static final String customTagPrefix = "xxx-ioc-";

    private LogIoc() {
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(Locale.CHINA, tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void v(String content) {
        if (!isDebug) {
            return;
        }
        String tag = generateTag();
        Log.v(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!isDebug) {
            return;
        }
        String tag = generateTag();

        Log.e(tag, content, tr);
    }

}
