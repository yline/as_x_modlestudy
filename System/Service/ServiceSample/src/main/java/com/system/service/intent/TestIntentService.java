package com.system.service.intent;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.system.service.same.SameService;
import com.yline.log.LogFileUtil;
import com.yline.utils.LogUtil;

/**
 * 并没有做测试;
 * IntentService是Service类的子类，用来处理异步请求,在onCreate()函数中
 * 通过HandlerThread单独开启一个线程来处理所有Intent请求对象,以免事务处理阻塞主线程
 *
 * @author yline 2016/11/10 --> 1:58
 * @version 1.0.0
 */
public class TestIntentService extends IntentService {
    public static void serviceStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TestIntentService.class);
        context.startService(intent);
    }

    public TestIntentService() {
        super("TestIntentService");
        LogUtil.v("construct pid = " + Process.myPid() + ", tid = " + Process.myTid());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 进程相同，线程不同
        LogUtil.v("onHandleIntent pid = " + Process.myPid() + ", tid = " + Process.myTid());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.v("onDestroy pid = " + Process.myPid() + ", tid = " + Process.myTid());

    }
}
