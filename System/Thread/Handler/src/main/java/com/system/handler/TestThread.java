package com.system.handler;

import com.system.handler.activity.IApplication;
import com.yline.log.LogFileUtil;

/**
 * 线程测试
 *
 * @author yline 2018/4/25 -- 21:05
 * @version 1.0.0
 */
public class TestThread extends Thread {
    private OnThreadCallback callback;

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 1000000000; i++) {
            if (i % 100000000 == 0) {
                LogFileUtil.v("TestThread = " + Thread.currentThread().getId() + ", i = " + i);

                final int finalI = i;
                IApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != callback) {
                            callback.onResult(finalI);
                        }
                    }
                });
            }
        }
    }

    public void setOnThreadCallback(OnThreadCallback callback) {
        this.callback = callback;
    }

    public interface OnThreadCallback {
        /**
         * 回值
         *
         * @param number 数值
         */
        void onResult(int number);
    }
}
