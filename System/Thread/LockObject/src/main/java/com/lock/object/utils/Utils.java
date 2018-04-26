package com.lock.object.utils;

import com.yline.log.LogFileUtil;

/**
 * 临时协助工具类
 *
 * @author yline 2018/4/26 -- 9:59
 * @version 1.0.0
 */
public class Utils {
    public static void doSleepLog(String location, int time) {
        LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", create sleep time = " + time);

        try {
            LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", try create time = " + time);
            Thread.sleep(time);
            LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", try finish time = " + time);
        } catch (InterruptedException e) {
            LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", exception time = " + time);
        }

        LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", finish sleep time = " + time);
    }

    public static void doSomeLog(String location, OnCallback callback) {
        LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", create");

        try {
            LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", try create");
            callback.call();
            LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", try finish");
        } catch (Exception e) {
            LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", exception");
        }

        LogFileUtil.v("Thread = " + Thread.currentThread().getName() + ", location = " + location + ", finish");
    }

    public interface OnCallback {
        /**
         * 调用
         *
         * @throws Exception 所有异常
         */
        void call() throws Exception;
    }
}
