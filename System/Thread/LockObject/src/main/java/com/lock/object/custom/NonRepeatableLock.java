package com.lock.object.custom;

/**
 * 不可重复锁
 * 同一个线程，不允许多次进入 一个代码块
 *
 * @author yline 2018/4/26 -- 15:39
 * @version 1.0.0
 */
public class NonRepeatableLock {
    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }
}
