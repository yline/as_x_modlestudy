package com.lock.object.custom;

import com.lock.object.utils.Utils;

/**
 * 测试类
 *
 * @author yline 2018/4/26 -- 16:40
 * @version 1.0.0
 */
public class Custom {
    public static void testRepeatable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RepeatableLock lock = new RepeatableLock();

                Utils.doSomeLog("lock first", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.lock();
                    }
                });

                Utils.doSomeLog("lock second", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.lock();
                    }
                });

                Utils.doSomeLog("unlock first", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.unlock();
                    }
                });

            }
        },"RepeatableLock").start();
    }

    public static void testNonRepeatable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final NonRepeatableLock lock = new NonRepeatableLock();

                Utils.doSomeLog("lock first", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.lock();
                    }
                });

                Utils.doSomeLog("lock second", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.lock();
                    }
                });

                Utils.doSomeLog("unlock first", new Utils.OnCallback() {
                    @Override
                    public void call() throws Exception {
                        lock.unlock();
                    }
                });
            }
        }, "NonRepeatableLock").start();
    }
}
